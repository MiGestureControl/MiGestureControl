import {Component} from 'angular2/core';
import {Device, DevicesService} from "../services/device.service";

@Component({
    selector: 'DeviceDetail',

    template: `
        test

    `
})

export class DeviceDetailComponent {

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

    edit(id: string) {
        this._devicesService.enterEditModeForDevice(id).subscribe(
            res => {
                console.log("Einrichtung erfolgt");
            },
            error => {
                console.log("fehler bei der Einrichtung aufgetretten");
            }
        );
    }
}
