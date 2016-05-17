
import {Component} from 'angular2/core';

@Component({
    selector: 'Docs',
    template: `

        <div class="slider fullscreen">
            <ul class="slides">
                <li>
                    <img onload="myFunction()" src="assets/img/handbuch_1.png"> <!-- random image -->
                    <div class="caption center-align">
                        <h5 class="light grey-text text-lighten-3">
                            Stellen Sie sich in den Erkennungsbereich Ihrer Kinect mit einem Abstand von 1-2 Metern.
                        </h5>
                    </div>
                </li>
                <li>
                    <img src="assets/img/handbuch_2.png"> <!-- random image -->
                    <div class="caption center-align">

                        <h5 class="light grey-text text-lighten-3">
                            Zeigen Sie mit einem Arm auf das Gerät, das eingerichtet werden soll.
                        </h5>
                    </div>
                </li>
                <li>
                    <img src="assets/img/handbuch_3.png"> <!-- random image -->
                    <div class="caption center-align">
                        <h5 class="light grey-text text-lighten-3">
                            Heben Sie den anderen Arm hoch, sodass dieser oberhalb Ihrer Schulter ist.
                        </h5>
                    </div>
                </li>
                <li>
                    <img src="assets/img/handbuch_4.png"> <!-- random image -->
                    <div class="caption center-align">
                        <h5 class="light grey-text text-lighten-3">
                            Bewegen Sie sich nun einen Meter nach rechts oder links. Die Position des Armes über der Schulter wird nicht verändert.
                            ...
                        </h5>
                    </div>
                </li>
                <li>
                    <img src="assets/img/handbuch_4.png"> <!-- random image -->
                    <div class="caption center-align">
                        <h5 class="light grey-text text-lighten-3">
                            ... Der Arm, der auf das Gerät zeigt, sollte nach wie vor auf das Gerät zeigen
                        </h5>
                    </div>
                </li>
                <li>
                    <img src="assets/img/handbuch_5.png"> <!-- random image -->
                    <div class="caption center-align">
                        <h5 class="light grey-text text-lighten-3">
                            Nun wird der Arm, der sich über Schulterhöhe befindet, gesenkt. Der andere Arm zeigt weiterhin auf das Gerät.
                        </h5>
                    </div>
                </li>
                <li>
                    <img src="assets/img/handbuch_6.png"> <!-- random image -->
                    <div class="caption center-align">
                        <h5 class="light grey-text text-lighten-3">
                        Nun können Sie den anderen Arm wird über Schulterhöhe heben. Sollte Ihr gewünschtes Gerät nun eingeschaltet sein,
                        ...
                        </h5>
                    </div>
                </li>
                <li>
                    <img src="assets/img/handbuch_6.png"> <!-- random image -->
                    <div class="caption center-align">
                        <h5 class="light grey-text text-lighten-3">
                        ... haben Sie alles richtig gemacht. Sollte dies nicht der Fall sein, so sollten Sie die Konfiguration noch einmal wiederholen.
                        </h5>
                    </div>
                </li>
            </ul>
        </div>


        `
})

export class DocsComponent {

}
