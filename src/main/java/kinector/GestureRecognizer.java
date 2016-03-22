package kinector;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import edu.ufl.digitalworlds.j4k.Skeleton;
import messages.GestureMessage;
import messages.SkeletonMessage;

import java.util.ArrayList;

/**
 * Created by johan on 16.02.2016.
 */
public class GestureRecognizer extends UntypedActor {

    private final ActorRef gestureRecognizerActor;

    public enum Gesture { RightHand_PointingTowardsDevice_DefaultActivate, LeftHand_PointingTowardsDevice_DefaultActivate,
        RightHand_PointingTowardsDevice_DefaultDeactivate, LeftHand_PointingTowardsDevice_DefaultDeactivate,
        BothHands_ActivateAll, BothHands_DeactivateAll, None }
    public enum Hand{
        RightHand_HigherThanHead, LeftHand_HigherThanHead,
        RightHand_LowerThanHead, LeftHand_LowerThanHead,
        RightHand_HigherThanHead_Upward, LeftHand_HigherThanHead_Upward,
        RightHand_LowerThanHead_Downward, LeftHand_LowerThanHead_Downward,
        RightHand_Pointer, LeftHand_Pointer,
        RightHand_Idle, LeftHand_Idle,
        BothHands_HigherThanHead_Crossed,
         BothHands_HigherThanHead_SidewardOutside,
        BothHands_HigherThanHead_Inside, BothHands_HigherThanHead_Outside
    }

    private static Hand[][] previousHandGestures = new Hand[][]{
            {Hand.RightHand_Idle, Hand.LeftHand_Idle},
            {Hand.RightHand_Idle, Hand.LeftHand_Idle},
            {Hand.RightHand_Idle, Hand.LeftHand_Idle},
            {Hand.RightHand_Idle, Hand.LeftHand_Idle},
            {Hand.RightHand_Idle, Hand.LeftHand_Idle},
            {Hand.RightHand_Idle, Hand.LeftHand_Idle}
    };

    public GestureRecognizer(ActorRef gestureRecognizerActor){
        super();
        this.gestureRecognizerActor = gestureRecognizerActor;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SkeletonMessage) {

            ArrayList<Skeleton> skeletons = ((SkeletonMessage) message).skeletons;

            for(Skeleton skeleton : skeletons) {

                Gesture detectedGesture = DetectGesture(skeleton);
                gestureRecognizerActor.tell(new GestureMessage(detectedGesture, skeleton), ActorRef.noSender());
            }
        }
    }

    private static boolean IsRightHandThePointer(Gesture gesture, Skeleton skeleton)
    {
        Hand[] handGestures = getHandGestures(skeleton);
        if (handGestures[0] == Hand.RightHand_Pointer){
            return true;
        }

        return false;

        //return (leftHand != Hand.LeftHand_LowerThanHead && leftHand != Hand.LeftHand_Idle);
    }

    public static Line GetLinePointingToDevice(Skeleton skeleton, Gesture gesture)
    {
        if (IsRightHandThePointer(gesture,skeleton))
            return new Line(skeleton.get3DJoint(Skeleton.ELBOW_RIGHT), skeleton.get3DJoint(Skeleton.HAND_RIGHT));
        else
            return new Line(skeleton.get3DJoint(Skeleton.ELBOW_LEFT), skeleton.get3DJoint(Skeleton.HAND_LEFT));
    }

    public static Gesture DetectGesture(Skeleton skeleton)
    {
        int skeletonID = skeleton.getPlayerID();
        Gesture gesture = Gesture.None;
        Hand[] handGestures = getHandGestures(skeleton);

        if(handGestures[0] == Hand.RightHand_Pointer){
            if(handGestures[1] == Hand.LeftHand_HigherThanHead_Upward){
                gesture = Gesture.RightHand_PointingTowardsDevice_DefaultActivate;
            }

            else if(handGestures[1] == Hand.LeftHand_LowerThanHead_Downward){
                gesture = Gesture.RightHand_PointingTowardsDevice_DefaultDeactivate;
            }
        }

        else if(handGestures[1] == Hand.LeftHand_Pointer){
            if(handGestures[0] == Hand.RightHand_HigherThanHead_Upward){
                gesture = Gesture.LeftHand_PointingTowardsDevice_DefaultActivate;
            }

            else if(handGestures[0] == Hand.RightHand_LowerThanHead_Downward){
                gesture = Gesture.LeftHand_PointingTowardsDevice_DefaultDeactivate;
            }
        }

        else if(handGestures[0] == Hand.BothHands_HigherThanHead_Crossed &&
                handGestures[1] == Hand.BothHands_HigherThanHead_Crossed){
                gesture = Gesture.BothHands_ActivateAll;
        }

        else if(handGestures[0] == Hand.BothHands_HigherThanHead_SidewardOutside &&
                handGestures[1] == Hand.BothHands_HigherThanHead_SidewardOutside){
            gesture = Gesture.BothHands_DeactivateAll;
        }

        if (gesture != Gesture.None)
        {
            previousHandGestures[skeletonID][0] = Hand.RightHand_Idle;
            previousHandGestures[skeletonID][1] = Hand.LeftHand_Idle;
        }

        return gesture;
    }

    public static Hand[] getHandGestures(Skeleton skeleton){
        int skeletonID = skeleton.getPlayerID();

        Hand[] handGestures = new Hand[2];
        handGestures[0] = Hand.RightHand_Idle;
        handGestures[1] = Hand.LeftHand_Idle;

        if((skeleton.get3DJointY(Skeleton.HAND_RIGHT) <= skeleton.get3DJointY(Skeleton.SPINE_MID)) &&
                (skeleton.get3DJointY(Skeleton.HAND_LEFT) <= skeleton.get3DJointY(Skeleton.SPINE_MID))){
            handGestures[0] = Hand.RightHand_Idle;
            handGestures[1] = Hand.LeftHand_Idle;
        }

        else if((skeleton.get3DJointY(Skeleton.HAND_RIGHT) >= skeleton.get3DJointY(Skeleton.HEAD)) &&
                (skeleton.get3DJointY(Skeleton.HAND_LEFT) >= skeleton.get3DJointY(Skeleton.HEAD))){

            //System.out.println((skeleton.get3DJointX(Skeleton.HAND_RIGHT) <= skeleton.get3DJointX(Skeleton.HAND_LEFT)));
            //System.out.println((skeleton.get3DJointX(Skeleton.HAND_LEFT) >= skeleton.get3DJointX(Skeleton.HAND_RIGHT)));
            if((skeleton.get3DJointX(Skeleton.HAND_RIGHT) <= skeleton.get3DJointX(Skeleton.HAND_LEFT)) &&
                    (skeleton.get3DJointX(Skeleton.HAND_LEFT) >= skeleton.get3DJointX(Skeleton.HAND_RIGHT))){

                handGestures[0] = Hand.BothHands_HigherThanHead_Crossed;
                handGestures[1] = Hand.BothHands_HigherThanHead_Crossed;

                /*else{
                    handGestures[0] = Hand.BothHands_HigherThanHead_Inside;
                    handGestures[1] = Hand.BothHands_HigherThanHead_Inside;
                }*/

            }

            else if((skeleton.get3DJointX(Skeleton.ELBOW_RIGHT) > skeleton.get3DJointX(Skeleton.SHOULDER_RIGHT) + 0.1) &&
                    (skeleton.get3DJointX(Skeleton.ELBOW_LEFT) < skeleton.get3DJointX(Skeleton.SHOULDER_LEFT) - 0.1)){
                if((previousHandGestures[skeletonID][0] == Hand.RightHand_Idle) &&
                        previousHandGestures[skeletonID][1] == Hand.LeftHand_Idle){
                    handGestures[0] = Hand.BothHands_HigherThanHead_SidewardOutside;
                    handGestures[1] = Hand.BothHands_HigherThanHead_SidewardOutside;
                }

                else{
                    handGestures[0] = Hand.BothHands_HigherThanHead_Outside;
                    handGestures[1] = Hand.BothHands_HigherThanHead_Outside;
                }
            }
        }

        else if(skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.NECK)) {
            if (skeleton.get3DJointZ(Skeleton.HAND_LEFT) < skeleton.get3DJointZ(Skeleton.HAND_RIGHT)) {
                handGestures[1] = Hand.LeftHand_Pointer;

                if (skeleton.get3DJointY(Skeleton.HAND_RIGHT) >= skeleton.get3DJointY(Skeleton.HEAD)) {
                    if (previousHandGestures[skeletonID][0] == Hand.RightHand_LowerThanHead) {
                        handGestures[0] = Hand.RightHand_HigherThanHead_Upward;
                    } else  {
                        handGestures[0] = Hand.RightHand_HigherThanHead;
                    }
                } else if (skeleton.get3DJointY(Skeleton.HAND_RIGHT) < skeleton.get3DJointY(Skeleton.HEAD)) {
                    if (previousHandGestures[skeletonID][0] == Hand.RightHand_HigherThanHead) {
                        handGestures[0] = Hand.RightHand_LowerThanHead_Downward;
                    } else {
                        handGestures[0] = Hand.RightHand_LowerThanHead;
                    }
                }
            }
        }

        else if(skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.NECK)) {
            if (skeleton.get3DJointZ(Skeleton.HAND_RIGHT) < skeleton.get3DJointZ(Skeleton.HAND_LEFT)) {

                handGestures[0] = Hand.RightHand_Pointer;

                if (skeleton.get3DJointY(Skeleton.HAND_LEFT) >= skeleton.get3DJointY(Skeleton.HEAD)) {
                    if (previousHandGestures[skeletonID][1] == Hand.LeftHand_LowerThanHead) {
                        handGestures[1] = Hand.LeftHand_HigherThanHead_Upward;
                    } else {
                        handGestures[1] = Hand.LeftHand_HigherThanHead;
                    }
                } else if (skeleton.get3DJointY(Skeleton.HAND_LEFT) < skeleton.get3DJointY(Skeleton.HEAD)) {
                    if (previousHandGestures[skeletonID][1] == Hand.LeftHand_HigherThanHead) {
                        handGestures[1] = Hand.LeftHand_LowerThanHead_Downward;
                    } else {
                        handGestures[1] = Hand.LeftHand_LowerThanHead;
                    }
                }
            }
        }

        previousHandGestures[skeletonID][0] = handGestures[0];
        previousHandGestures[skeletonID][1] = handGestures[1];

        //System.out.println(handGestures[0]);
        //System.out.println(handGestures[1]);

        return handGestures;
    }

    /*public static Gesture DetectGesture(Skeleton skeleton)
    {
        int skeletonID = skeleton.getPlayerID();
        Gesture gesture = Gesture.None;
        Hand rightHandState = GetRightHandState(skeleton);
        Hand leftHandState = GetLeftHandState(skeleton);

        if (rightHandState == Hand.RightHand_HigherThanHead_RightLeft)
        {
            gesture = Gesture.LeftHandPointingToDevice_on;
        }
        else if (leftHandState == Hand.LeftHand_HigherThanHead_LeftRight)
        {
            gesture = Gesture.RightHandPointingToDevice_on;
        }
        else if (rightHandState == Hand.RightHand_HigherThanShoulder_LeftRight)
        {
            gesture = Gesture.LeftHandPointingToDevice_off;
        }
        else if (leftHandState == Hand.LeftHand_HigherThanShoulder_RightLeft)
        {
            gesture = Gesture.RightHandPointingToDevice_off;
        }

        if (gesture != Gesture.None)
        {
            previousHands[skeletonID][0] = Hand.RightHand_Idle;
            previousHands[skeletonID][1] = Hand.LeftHand_Idle;
        }

        return gesture;
    }

    public static Hand GetRightHandState(Skeleton skeleton)
    {
        int skeletonID = skeleton.getPlayerID();
        Hand rightHandState = Hand.RightHand_Idle;

        if (skeleton.get3DJointY(Skeleton.HAND_RIGHT) < skeleton.get3DJointY(Skeleton.HEAD))
        {
            rightHandState = Hand.RightHand_LowerThanHead;
        }
        else if (skeleton.get3DJointX(Skeleton.HAND_RIGHT) >= skeleton.get3DJointX(Skeleton.SHOULDER_RIGHT))
        {
            rightHandState = previousHands[skeletonID][0] == Hand.RightHand_HigherThanLeftShoulder || previousHands[skeletonID][0] == Hand.RightHand_HigherThanShoulder_LeftRight ? Hand.RightHand_HigherThanShoulder_LeftRight : Hand.RightHand_HigherThanRightShoulder;
        }
        else if (skeleton.get3DJointX(Skeleton.HAND_RIGHT) < skeleton.get3DJointX(Skeleton.SHOULDER_RIGHT))
        {
            rightHandState = previousHands[skeletonID][0] == Hand.RightHand_HigherThanRightShoulder || previousHands[skeletonID][0] == Hand.RightHand_HigherThanHead_RightLeft ? Hand.RightHand_HigherThanHead_RightLeft : Hand.RightHand_HigherThanLeftShoulder;
        }

        previousHands[skeletonID][0] = rightHandState;
        return rightHandState;
    }

    public static Hand GetLeftHandState(Skeleton skeleton)
    {
        int skeletonID = skeleton.getPlayerID();
        Hand leftHandState = Hand.LeftHand_Idle;

        if (skeleton.get3DJointY(Skeleton.HAND_LEFT) < skeleton.get3DJointY(Skeleton.HEAD))
        {
            leftHandState = Hand.LeftHand_LowerThanHead;
        }
        else if (skeleton.get3DJointX(Skeleton.HAND_LEFT) <= skeleton.get3DJointX(Skeleton.SHOULDER_LEFT))
        {
            leftHandState = previousHands[skeletonID][1] == Hand.LeftHand_HigherThanLeftShoulder || previousHands[skeletonID][1] == Hand.LeftHand_HigherThanShoulder_RightLeft ? Hand.LeftHand_HigherThanShoulder_RightLeft : Hand.LeftHand_HigherThanRightShoulder;
        }
        else if (skeleton.get3DJointX(Skeleton.HAND_LEFT) > skeleton.get3DJointX(Skeleton.SHOULDER_LEFT))
        {
            leftHandState = previousHands[skeletonID][1] == Hand.LeftHand_HigherThanRightShoulder || previousHands[skeletonID][1] == Hand.LeftHand_HigherThanHead_LeftRight ? Hand.LeftHand_HigherThanHead_LeftRight : Hand.LeftHand_HigherThanLeftShoulder;
        }

        previousHands[skeletonID][1] = leftHandState;
        return leftHandState;
    }*/


}

