import {Component} from 'angular2/core';
import {Device, DevicesService} from "../services/device.service";
import {RouteParams} from 'angular2/router';

@Component({
    selector: 'DeviceDetail',

    template: `
            <h3>test</h3>
            <div class="row">
                <div class="col s6 m12">
                    <div class="card">
                        <div class="card-content">
                            <span class="card-title">Linke Hand konfigurieren</span>
                            <p>I am a very simple card. I am good at containing small bits of information.
                            I am convenient because I require little markup to use effectively.</p>
                        </div>
                        <div class="card-action">
                          <a class="waves-effect waves-light btn" (click)="addLeft(device.id)"  ><i class="material-icons">add</i></a>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-content">
                            <span class="card-title">Rechte Hand konfigurieren</span>
                            <p>I am a very simple card. I am good at containing small bits of information.
                            I am convenient because I require little markup to use effectively.</p>
                        </div>
                        <div class="card-action">
                            <a class="waves-effect waves-light btn" (click)="addRight(device.id)"  ><i class="material-icons">add</i></a>
                        </div>
                    </div>
                </div>
            </div>
    `
})

export class DeviceDetailComponent {

    private devices: Array<Device> = [];
    private interval;

    constructor(private _routeParams: RouteParams,
                private _devicesService: DevicesService) {

        this._devicesService.devicesSubject
            .subscribe( devices => {
                    this.devices = devices;
                }
            );
        this._devicesService.load();
        this.interval = setInterval(() => this._devicesService.load(), 5000 );
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
