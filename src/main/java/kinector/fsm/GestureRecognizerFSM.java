package kinector.fsm;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import edu.ufl.digitalworlds.j4k.Skeleton;
import kinector.GestureRecognizer;
import messages.GestureMessage;
import messages.HelperEnums.Hand;
import messages.SingleSkeletonMessage;
import messages.SkeletonStateMessage;

/**
 * Übergeordneter Zustandsautomaten-Aktor für die Erkennung von Gesten
 * Für die Interpretation der Skelettdaten werden Sub-Automaten verwendet, die in sich gekapselt
 * einzelne Bewegungs- und/oder Positionsinformation verarbeiten und daraus Rückschlüsse auf
 * eine übergeordnete Skelett-Geste ziehen.
 */
public class GestureRecognizerFSM extends UntypedActor {

    /** Aufzählung der möglichen erkannten Gesten. ("None", wenn keine Geste erkannt wurde.) */
    public enum Gesture {
        BothHands_ActivateAll,      // "Alle Geräte aktivieren" erkannt
        BothHands_DeactivateAll,    // "Alle Geräte deaktivieren" erkannt
        None,                       // keine Geste erkannt
        /* NEU */
        Pointing,           // Zeigende Hand erkannt
        ActivateDevice,     // "Gerät aktivieren" erkannt
        DeactivateDevice,   // "Gerät deaktivieren" erkannt
        StretchedUp,        // Nach oben gestreckter Arm erkannt
    }

    /** Aufzählung der übergeordneten Automaten-Zustände **/
    private enum State {
        IDLE,                   // "Ruhezustand"

        LEFT_CONTROLLING,       // Rechte Hand zeigt, linke Hand kontrolliert Gerätezustand
        RIGHT_CONTROLLING,      // Linke Hand zeigt, rechte Hand kontrolliert Gerätezustand
        
        DEVICE_ON,              // Zustand des gewählten Geräts nach "An" ändern
        DEVICE_OFF,             // Zustand des gewählten Geräts nach "Aus" ändern

        ALL_DEVICES_ON,         // Alle Geräte nach "An" ändern
        ALL_DEVICES_OFF,        // Alle Geräte nach "Aus" ändern
        
        PLAY_SOUND,             // Soundsteuerung auslösen
    }

    /** Ziel-Aktor der von der Gestenerkennung zurück gesendeten Nachrichten **/
    private ActorRef messageTarget;

    /** Zwischenspeicher für Skelettdaten **/
    private Skeleton[] skeletons;

    /** Zwischenspeicher für Zustand (ein Feldeintrag je möglichem Skelett) **/
    private State[] currentState;

    /** Sub-Automat zur Erkennung der Zeige-Geste ("Geräteauswahl") **/
    private ActorRef fsmPointing;

    /** Sub-Automat zur Erkennung der Geste zur Geräte-Zustandsänderung **/
    private ActorRef fsmRaising;

    /** Sub-Automat zur Erkennung der Geste zum Schalten aller Geräte **/
    private ActorRef fsmCrossing;

    /** Sub-Automat für Erkennung der Soundsteuerungs-Geste **/
    private ActorRef fsmFlashing;

    /**
     * Konstruktor, initialisiert den Zustandsautomaten sowie sein Sub-Automaten und
     * bereitet Zwischenspeicher für das Ablegen der Skelettdaten und Zustände vor
     * @param _messageTarget Ziel-Aktor der von der Gestenerkennung zurück gesendeten Nachrichten
     */
    public GestureRecognizerFSM(ActorRef _messageTarget)
    {
        messageTarget = _messageTarget;
        skeletons = new Skeleton[7];
        currentState = new State[] {
                State.IDLE, State.IDLE, State.IDLE, State.IDLE,
                State.IDLE, State.IDLE, State.IDLE,
        };

        registerGestureStateMachines();
    }

    /**
     * Führt den Zustandsübergang für das Skelett mit der übergebenen playerID durch
     * @param playerID Eindeutiger Identifier des betroffenen Skeletts
     * @param state Neuer Zustand
     */
    private void setState(int playerID, State state)
    {
        if(currentState[playerID] != state) {
            currentState[playerID] = transition(playerID, currentState[playerID], state);
        }
    }

    /**
     * Liefert den aktuellen Automaten-Zustand für das Skelett mit der übergebenen playerID
     * @param playerID Eindeutiger Identifier des betroffenen Skeletts
     * @return Aktueller Zustand des betroffenen Skeletts
     */
    private State getState(int playerID) {
        return currentState[playerID];
    }

    /**
     * Zustandsübergangsfunktion des Automaten
     * Anhand der übergebenen playerID des betroffenen Skeletts wird der Zustand aktualisiert, bei
     * Erreichen bestimmter Zustände wird an den bei der Erstellung des Automaten festgelegten
     * Empfänger eine bestimmte (erkannte) Geste übermittelt. Bei Zuständen, deren Präsenz nicht
     * dauerhaft sein soll, wird der tatsächliche Nachfolgezustand auf IDLE festgelegt.
     * @param playerID Eindeutiger Identifier des betroffenen Skeletts
     * @param current Aktueller Zustand des betroffenen Skeletts
     * @param next Erkannter, nächster Zustand (wird u.U. durch die Zustandsübergangsfunktion geändert)
     * @return Der tatsächliche nächste Zustand des betroffenen Skeletts
     */
    private State transition(int playerID, State current, State next) {
        switch(next)
        {
            case DEVICE_ON:
            {
                // Sende "ActivateDevice"-Geste an den Gesten-Interpreter
                Hand pointingHand = current == State.LEFT_CONTROLLING ? Hand.RIGHT : Hand.LEFT;
                messageTarget.tell(new GestureMessage(GestureRecognizer.Gesture.ActivateDevice,
                        skeletons[playerID], pointingHand), ActorRef.noSender());
                // Geste ist vollständig abgeschlossen, setze Zustand des Automaten in den
                // Ruhezustand zurück
                next = State.IDLE;
                break;
            }
            
            case DEVICE_OFF:
            {
                // Sende "DeactivateDevice"-Geste an den Gesten-Interpreter
                Hand pointingHand = current == State.LEFT_CONTROLLING ? Hand.RIGHT : Hand.LEFT;
                messageTarget.tell(new GestureMessage(GestureRecognizer.Gesture.DeactivateDevice,
                        skeletons[playerID], pointingHand), ActorRef.noSender());
                // Geste ist vollständig abgeschlossen, setze Zustand des Automaten in den
                // Ruhezustand zurück
                next = State.IDLE;
                break;
            }

            case ALL_DEVICES_ON:
            {
                // Sende "BothHands_ActivateAll"-Geste an den Gesten-Interpreter
                messageTarget.tell(new GestureMessage(GestureRecognizer.Gesture.BothHands_ActivateAll,
                        skeletons[playerID], Hand.BOTH), ActorRef.noSender());
                break;
            }

            case ALL_DEVICES_OFF:
            {
                // Sende "BothHands_DeactivateAll"-Geste an den Gesten-Interpreter
                messageTarget.tell(new GestureMessage(GestureRecognizer.Gesture.BothHands_DeactivateAll,
                        skeletons[playerID], Hand.BOTH), ActorRef.noSender());
                break;
            }

            case PLAY_SOUND:
            {
                // Sende "StretchedUp"-Geste an den Gesten-Interpreter
                messageTarget.tell(new GestureMessage(GestureRecognizer.Gesture.StretchedUp,
                        skeletons[playerID], Hand.UNKNOWN), ActorRef.noSender());
                break;
            }
        }
        return next;
    }

    /**
     * Registrierung der agierenden Sub-Automaten für die Erkennung von Teilgesten
     */
    private void registerGestureStateMachines()
    {
        fsmPointing = (createGestureStateMachine(GestureRecognizerFSMHandPointing.class));
        fsmRaising = (createGestureStateMachine(GestureRecognizerFSMHandRaising.class));
        fsmCrossing = (createGestureStateMachine(GestureRecognizerFSMHandsCrossing.class));
        fsmFlashing = (createGestureStateMachine(GestureRecognizerFSMHandFlashing.class));
    }

    /**
     * Erzeugt einen Aktor für den angegebenen Zustandsautomaten-Typ
     * @param stateMachineClass Zustandsautomaten-Typ, von dem eine Instanz erzeugt werden soll
     * @return Aktoren-Referenz die den Automaten im Aktoren-System darstellt
     */
    private ActorRef createGestureStateMachine(Class<? extends AbstractGestureRecognizerFSM> stateMachineClass) {
        return getContext().system().actorOf(Props.create(stateMachineClass));
    }

    /**
     * Behandlungsroutine für Skelettstatus, welcher aus den Sub-Automaten geliefert wird
     * @param message Nachrichtenobjekt, welches den Skelettstatus enthält
     */
    private void handleSkeletonStateMessage(SkeletonStateMessage message) {
        int skeletonID = message.skeleton.getPlayerID();
        skeletons[skeletonID] = message.skeleton;
        State currentState = getState(skeletonID);

        switch(currentState) {
            case IDLE:
            {
                // Im Ruhezustand sind folgende Übergänge möglich:
                // -> RIGHT_CONTROLLING, wenn linke Hand auf ein Gerät zeigt (rechte Hand steuert Gerät)
                // -> LEFT_CONTROLLING, wenn rechte Hand auf ein Gerät zeigt (linke Hand steuert Gerät)
                // -> ALL_DEVICES_ON, wenn Hände oberhalb des Kopfes überkreuzt sind
                // -> PLAY_SOUND, wenn linke oder rechte Hand gerade nach oben zeigt

                if(message.affectedHand == Hand.LEFT && message.gesture == GestureRecognizer.Gesture.Pointing)
                    setState(skeletonID, State.RIGHT_CONTROLLING);
                else if(message.affectedHand == Hand.RIGHT && message.gesture == GestureRecognizer.Gesture.Pointing)
                    setState(skeletonID, State.LEFT_CONTROLLING);
                else if(message.affectedHand == Hand.BOTH && message.gesture == GestureRecognizer.Gesture.BothHands_ActivateAll)
                    setState(skeletonID, State.ALL_DEVICES_ON);
                else if(message.affectedHand == Hand.LEFT && message.gesture == GestureRecognizer.Gesture.StretchedUp)
                    setState(skeletonID, State.PLAY_SOUND);
                else if(message.affectedHand == Hand.RIGHT && message.gesture == GestureRecognizer.Gesture.StretchedUp)
                    setState(skeletonID, State.PLAY_SOUND);
                break;
            }

            case LEFT_CONTROLLING:
            {
                // Im Zustand LEFT_CONTROLLING sind folgende Übergänge möglich:
                // -> DEVICE_ON, linke (steuernde) Hand wurde oberhalb des Kopfes gehoben
                // -> DEVICE_OFF, linke (steuernde) Hand wurde unter den Kopf gesenkt
                // -> PLAY_SOUND, rechte (zeigende) Hand wird aus der Zeigeposition gerade nach oben gestreckt

                if(message.affectedHand == Hand.LEFT && message.gesture == GestureRecognizer.Gesture.ActivateDevice)
                    setState(skeletonID, State.DEVICE_ON);
                else if(message.affectedHand == Hand.LEFT && message.gesture == GestureRecognizer.Gesture.DeactivateDevice)
                    setState(skeletonID, State.DEVICE_OFF);
                else if(message.affectedHand == Hand.RIGHT && message.gesture == GestureRecognizer.Gesture.StretchedUp)
                    setState(skeletonID, State.PLAY_SOUND);
                break;
            }

            case RIGHT_CONTROLLING:
            {
                // Im Zustand RIGHT_CONTROLLING sind folgende Übergänge möglich:
                // -> DEVICE_ON, rechte (steuernde) Hand wurde oberhalb des Kopfes gehoben
                // -> DEVICE_OFF, rechte (steuernde) Hand wurde unter den Kopf gesenkt
                // -> PLAY_SOUND, linke (zeigende) Hand wird aus der Zeigeposition gerade nach oben gestreckt

                if (message.affectedHand == Hand.RIGHT && message.gesture == GestureRecognizer.Gesture.ActivateDevice)
                    setState(skeletonID, State.DEVICE_ON);
                else if (message.affectedHand == Hand.RIGHT && message.gesture == GestureRecognizer.Gesture.DeactivateDevice)
                    setState(skeletonID, State.DEVICE_OFF);
                else if(message.affectedHand == Hand.LEFT && message.gesture == GestureRecognizer.Gesture.StretchedUp)
                    setState(skeletonID, State.PLAY_SOUND);
                break;
            }

            case ALL_DEVICES_ON:
            {
                // Im Zustand ALL_DEVICES_ON sind folgende Übergänge möglich:
                // -> ALL_DEVICES_OFF, wenn Hände oberhalb des Kopfes entkreuzt wurden
                // -> IDLE, wenn Hände nach der Überkreuzung herunter genommen werden

                if(message.affectedHand == Hand.BOTH && message.gesture == GestureRecognizer.Gesture.BothHands_DeactivateAll)
                    setState(skeletonID, State.ALL_DEVICES_OFF);
                if(message.affectedHand == Hand.BOTH && message.gesture == GestureRecognizer.Gesture.None)
                    setState(skeletonID, State.IDLE);
                break;
            }

            case ALL_DEVICES_OFF:
            {
                // Im Zustand ALL_DEVICES_OFF sind folgende Übergänge möglich:
                // -> ALL_DEVICES_ON, wenn Hände oberhalb des Kopfes gekreuzt werden
                // -> IDLE, wenn Hände nach der Entkreuzung herunter genommen werden

                if(message.affectedHand == Hand.BOTH && message.gesture == GestureRecognizer.Gesture.BothHands_ActivateAll)
                    setState(skeletonID, State.ALL_DEVICES_ON);
                if(message.affectedHand == Hand.BOTH && message.gesture == GestureRecognizer.Gesture.None)
                    setState(skeletonID, State.IDLE);
                break;
            }
        }
    }

    /**
     * Nachrichten-Empfänger-Funktion des Aktors
     * Behandelt werden Skelettdaten des Kinectors sowie Rückantworten des Sub-Automaten zur
     * Veränderung der Skelett-Zustände innerhalb dieses übergeordneten Automaten
     * @param message Nachrichten-Objekt
     * @throws Exception
     */
    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof SingleSkeletonMessage) {
            Skeleton skeleton = ((SingleSkeletonMessage)message).skeleton;
            State currentState = getState(skeleton.getPlayerID());

            // Skelettdaten-Nachricht wird je nach Zustand an die möglichen
            // Sub-Automaten weitergeleitet, die für Folgezustände zuständig sein können
            switch(currentState)
            {
                case IDLE:
                {
                    fsmFlashing.tell(message, getSelf());
                    fsmCrossing.tell(message, getSelf());
                    fsmPointing.tell(message, getSelf());
                    break;
                }
                case LEFT_CONTROLLING:
                case RIGHT_CONTROLLING:
                {
                    ((SingleSkeletonMessage)message).hand = currentState == State.LEFT_CONTROLLING ? Hand.LEFT : Hand.RIGHT;
                    fsmFlashing.tell(message, getSelf());
                    fsmRaising.tell(message, getSelf());
                    break;
                }
                case ALL_DEVICES_ON:
                case ALL_DEVICES_OFF:
                {
                    fsmCrossing.tell(message, getSelf());
                    break;
                }
            }

        }
        else if(message instanceof SkeletonStateMessage) {
            // Skelettzustand-Nachricht wird an separate Funktion weitergegebn
            handleSkeletonStateMessage((SkeletonStateMessage)message);
        }

        unhandled(message);
    }
}
