import 'rxjs/add/operator/map';
import 'rxjs/add/operator/delay';
import 'rxjs/add/operator/mergeMap';
import 'rxjs/add/observable/interval';
import 'rxjs/add/observable/forkJoin';

import 'zone.js';
import 'reflect-metadata';

import {enableProdMode} from "angular2/core";
import {Component, provide} from 'angular2/core';
import {ROUTER_DIRECTIVES,
    RouteConfig,
    ROUTER_PROVIDERS,
    LocationStrategy,
    HashLocationStrategy,
    Router} from 'angular2/router';
import {CORE_DIRECTIVES} from "angular2/common";
import {DevicesComponent} from "./components/devicelist.component.ts";
import {RouterOutlet} from "angular2/router";
import {DocsComponent} from "./components/docs.component";

declare var System: any;

@Component({
    selector: 'app',
    directives: [
        ROUTER_DIRECTIVES,
        CORE_DIRECTIVES
    ],
    template: `
<div class="navbar-fixed">
    <nav>
        <div class="nav-wrapper">
            <a href="#" class="brand-logo">
                <div>MiGestureControl</div>
            </a>
            <a href="#" data-activates="mobile-demo" class="button-collapse"><i class="material-icons">menu</i></a>

            <ul id="nav-mobile" class="right hide-on-med-and-down">
                <li><a [routerLink]="['/Devices']" >Geräte</a></li>
                <li><a [routerLink]="['/Docs']" >Handbuch</a></li>
            </ul>

            <ul class="side-nav" id="mobile-demo">
                <li><a [routerLink]="['/Devices']" >Geräte</a></li>
                <li><a [routerLink]="['/Docs']" >Handbuch</a></li>
            </ul>

        </div>
    </nav>
</div>
<main class="mdl-layout__content">
    <router-outlet ></router-outlet>
</main>
        `
})

@RouteConfig([
    {
        path: '/',
        component: DevicesComponent,
        name: 'Devices'
    },
    {
        path: '/docs',
        component: DocsComponent,
        name: 'Docs'
    }
])

export class App {

    constructor(private _router: Router) {
        // enableProdMode();
    }

}
