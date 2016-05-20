import {Component} from 'angular2/core';
import {Device, DevicesService} from "../services/device.service";
import {RouteParams} from 'angular2/router';
import {indexOfId} from "../services/comon";
import {PossibleSet} from "../services/device.service";

@Component({
    selector: 'DeviceDetail',

    template: `
            <h3>{{id}}</h3>
            <div class="row">
                <div class="col s6 m12">
                    <div class="card">
                        <div class="card-content">
                            <span class="card-title">Linke Hand konfigurieren</span>
                            <p>I am a very simple card. I am good at containing small bits of information.
                            I am convenient because I require little markup to use effectively.</p>
                        </div>
                        <div class="card-action">
                          <a class="waves-effect waves-light btn" (click)="addLeft(id)"  ><i class="material-icons">add</i></a>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-content">
                            <span class="card-title">Rechte Hand konfigurieren</span>
                            <p>I am a very simple card. I am good at containing small bits of information.
                            I am convenient because I require little markup to use effectively.</p>
                        </div>
                        <div class="card-action">
                            <a class="waves-effect waves-light btn" (click)="addRight(id)"  ><i class="material-icons">add</i></a>
                        </div>
                    </div>
                        <div class="card">
                           <div class="card-content">
                               <span class="card-title">Sets</span>
                                <ul class="collection">
                                    <li *ngFor="#possibleSet of possibleSets" class="collection-item avatar">
                                        {{possibleSet.name}}
                                         <ul class="collection">
                                            <li *ngFor="#arg of possibleSet.args" class="collection-item avatar">
                                                {{arg}}
                                            </li>
                                         </ul>
                                    </li>
                                </ul>
                           </div>
                           <div class="card-action">
                               <a class="waves-effect waves-light btn" (click)="addRight(id)"  ><i class="material-icons">add</i></a>
                           </div>
                    </div>
                </div>
            </div>
    `
})

export class DeviceDetailComponent {

    private interval;
    private id:string;
    private device:Device;
    private possibleSets:PossibleSet[];

    constructor(private _routeParams: RouteParams,
                private _devicesService: DevicesService) {

        this.id = this._routeParams.get('id');

        this._devicesService.devicesSubject
            .subscribe( devices => {
                var index indexOfId(devices, this.id);
                this.device = devices[index];
                if (this.device !== null){
                    if (this.device.possibleSets !== null){
                        this.possibleSets = this.device.possibleSets;
                    }
                }

                }
            );
        this._devicesService.load();
        //this.interval = setInterval(() => this._devicesService.load(), 5000 );
    }

    addRight(id: string) {
        this._devicesService.enterRightEditModeForDevice(id).subscribe(
            res => {
                console.log("Einrichtung erfolgt");
            },
            error => {
                console.log("fehler bei der Einrichtung aufgetretten");
            }
        );
    }

    addLeft(id: string) {
        this._devicesService.enterLeftEditModeForDevice(id).subscribe(
            res => {
                console.log("Einrichtung erfolgt");
            },
            error => {
                console.log("fehler bei der Einrichtung aufgetretten");
            }
        );
    }

    delete(id: string) {
        this._devicesService.deleteDevice(id).subscribe(
            res => {
                console.log("gelöscht");
            },
            error => {
                console.log("fehler beim löschen aufgetretten");
            }
        );
    }
}
