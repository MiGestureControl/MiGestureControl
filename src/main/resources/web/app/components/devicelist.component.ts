import {Component} from 'angular2/core';
import {Device, DevicesService} from "../services/device.service";

@Component({
    selector: 'Devices',
    template: `
        <ul class="collection">
            <li *ngFor="#device of devices" class="collection-item avatar">
                <i class="material-icons circle"><i class="material-icons">local_florist</i></i>
                <span class="title">{{device.id}}</span>
                <p>{{device.state}}<br>
                    Second Line
                </p>
                <div class="secondary-content">
                    <a (click)="add(device.id)"  ><i class="material-icons">add</i></a>
                    <a (click)="edit(device.id)" ><i class="material-icons">mode_edit</i></a>
                </div>
            </li>
        </ul>

    `
})

export class DevicesComponent {

    private devices: Array<Device> = [];
    private interval;

    constructor(private _devicesService: DevicesService) {

        this._devicesService.devicesSubject
            .subscribe( devices => {
                    this.devices = devices;
                }
            );
        this._devicesService.load();
        this.interval = setInterval(() => this._devicesService.load(), 5000 );
    }

    add(id: string) {
        this._devicesService.enterEditModeForDevice(id).subscribe(
            res => {
                console.log("Einrichtung erfolgt");
            },
            error => {
                console.log("fehler bei der Einrichtung aufgetretten");
            }
        );
    }

    edit(id: string) {
        console.log("edit geklickt");
    }
}
