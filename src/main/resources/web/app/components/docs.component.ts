
import {Component} from 'angular2/core';

@Component({
    selector: 'Docs',
    template: `

        <div class="slider fullscreen">
            <ul class="slides">
                <li>
                    <img onload="myFunction()" src="assets/img/handbuch1.png"> <!-- random image -->
                    <div class="caption right-align">
                        <h5 class="light grey-text text-lighten-3">
                            Stellen Sie sich in den Erkennungsbereich Ihrer Kinect mit einem Abstand von 1-2 Metern.
                        </h5>
                    </div>
                </li>
                <li>
                    <img src="assets/img/handbuch2.png"> <!-- random image -->
                    <div class="caption left-align">
                        <h5 class="light grey-text text-lighten-3">
                            Zeigen Sie mit einem Arm auf das Gerät, das eingerich- tet werden soll.
                        </h5>
                    </div>
                </li>
                <li>
                    <img src="assets/img/handbuch3.png"> <!-- random image -->
                    <div class="caption right-align">
                        <h5 class="light grey-text text-lighten-3">
                            eben Sie den anderen Arm hoch, sodass dieser ober- halb Ihrer Schulter ist.
                        </h5>
                    </div>
                </li>
            </ul>
        </div>


        `
})

export class DocsComponent {

}
