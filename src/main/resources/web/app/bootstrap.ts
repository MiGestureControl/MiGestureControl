import {bootstrap} from 'angular2/platform/browser';
import {provide} from 'angular2/core';
import {HTTP_PROVIDERS, Http} from 'angular2/http';
import {DevicesService} from "./services/device.service";
import {ROUTER_PROVIDERS, LocationStrategy, HashLocationStrategy} from "angular2/router";

import {App} from './app';

bootstrap(App, [
    HTTP_PROVIDERS,
    ROUTER_PROVIDERS,
    DevicesService,
    provide(
        LocationStrategy,
        {useClass: HashLocationStrategy}
    )
]);
