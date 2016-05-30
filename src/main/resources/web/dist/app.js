webpackJsonp([0],{

/***/ 0:
/***/ function(module, exports, __webpack_require__) {

	"use strict";
	var browser_1 = __webpack_require__(1);
	var core_1 = __webpack_require__(24);
	var http_1 = __webpack_require__(229);
	var device_service_1 = __webpack_require__(244);
	var router_1 = __webpack_require__(246);
	var app_1 = __webpack_require__(275);
	browser_1.bootstrap(app_1.App, [
	    http_1.HTTP_PROVIDERS,
	    router_1.ROUTER_PROVIDERS,
	    device_service_1.DevicesService,
	    core_1.provide(router_1.LocationStrategy, { useClass: router_1.HashLocationStrategy })
	]);


/***/ },

/***/ 244:
/***/ function(module, exports, __webpack_require__) {

	"use strict";
	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(24);
	var http_1 = __webpack_require__(229);
	var comon_1 = __webpack_require__(245);
	var Subject_1 = __webpack_require__(53);
	var DevicesService = (function () {
	    function DevicesService(_http) {
	        this._http = _http;
	        this.devicesSubject = new Subject_1.Subject();
	        this.deviceEditSubject = new Subject_1.Subject();
	        this.apiBaseUrl = 'api/v1';
	        this._devices = [];
	    }
	    DevicesService.prototype.load = function () {
	        var _this = this;
	        return this._http.get(this.apiBaseUrl + "/devices", { headers: comon_1.headers() })
	            .map(function (res) { return res.json(); })
	            .subscribe(function (devices) {
	            _this.setDevices(devices);
	        }, function (error) {
	            console.log(error);
	        });
	    };
	    DevicesService.prototype.enterRightEditModeForDevice = function (id) {
	        return this._http.get(this.apiBaseUrl + "/devices/" + id, { headers: comon_1.headers() })
	            .map(function (res) { return res.json(); });
	    };
	    DevicesService.prototype.enterLeftEditModeForDevice = function (id) {
	        return this._http.get(this.apiBaseUrl + "/devicesleft/" + id, { headers: comon_1.headers() })
	            .map(function (res) { return res.json(); });
	    };
	    DevicesService.prototype.deleteDevice = function (id) {
	        return this._http.delete(this.apiBaseUrl + "/devices/" + id, { headers: comon_1.headers() })
	            .map(function (res) { return res.json(); });
	    };
	    DevicesService.prototype.save = function (id, activSets) {
	        var body = JSON.stringify(activSets);
	        console.log("save: " + body);
	        return this._http.post(this.apiBaseUrl + "/devices/" + id, body, { headers: comon_1.headers() })
	            .map(function (res) { return res.json(); })
	            .subscribe(function (res) {
	            console.log(res);
	        });
	    };
	    DevicesService.prototype.setDevices = function (devices) {
	        this._devices = devices;
	        this.devicesSubject.next(this._devices);
	    };
	    DevicesService = __decorate([
	        core_1.Injectable(), 
	        __metadata('design:paramtypes', [http_1.Http])
	    ], DevicesService);
	    return DevicesService;
	}());
	exports.DevicesService = DevicesService;
	var Device = (function () {
	    function Device() {
	    }
	    return Device;
	}());
	exports.Device = Device;
	var PossibleSet = (function () {
	    function PossibleSet() {
	    }
	    return PossibleSet;
	}());
	exports.PossibleSet = PossibleSet;
	var ActivSet = (function () {
	    function ActivSet(name, arg) {
	        this.name = name;
	        this.arg = arg;
	    }
	    return ActivSet;
	}());
	exports.ActivSet = ActivSet;
	var ActivSets = (function () {
	    function ActivSets(activSetTrunOffGesture, activSetTrunOnGesture) {
	        this.activSetTrunOffGesture = activSetTrunOffGesture;
	        this.activSetTrunOnGesture = activSetTrunOnGesture;
	    }
	    return ActivSets;
	}());
	exports.ActivSets = ActivSets;


/***/ },

/***/ 245:
/***/ function(module, exports, __webpack_require__) {

	"use strict";
	var http_1 = __webpack_require__(229);
	function headers() {
	    var headers = new http_1.Headers();
	    headers.append('Content-Type', 'application/json');
	    return headers;
	}
	exports.headers = headers;
	function indexOfId(array, id) {
	    var length = array.length;
	    for (var i = 0; i < length; i++) {
	        if (array[i].id === id) {
	            return i;
	        }
	    }
	    return -1;
	}
	exports.indexOfId = indexOfId;


/***/ },

/***/ 275:
/***/ function(module, exports, __webpack_require__) {

	"use strict";
	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	__webpack_require__(276);
	__webpack_require__(278);
	__webpack_require__(291);
	__webpack_require__(297);
	__webpack_require__(300);
	__webpack_require__(303);
	__webpack_require__(304);
	var core_1 = __webpack_require__(24);
	var router_1 = __webpack_require__(246);
	var common_1 = __webpack_require__(118);
	var devicelist_component_ts_1 = __webpack_require__(305);
	var docs_component_1 = __webpack_require__(307);
	var deviceDetail_component_1 = __webpack_require__(308);
	var App = (function () {
	    function App(_router) {
	        this._router = _router;
	    }
	    App = __decorate([
	        core_1.Component({
	            selector: 'app',
	            directives: [
	                router_1.ROUTER_DIRECTIVES,
	                common_1.CORE_DIRECTIVES
	            ],
	            template: "\n<div class=\"navbar-fixed\">\n    <nav>\n        <div class=\"nav-wrapper\">\n            <a href=\"#\" class=\"brand-logo\">\n                <div>MiGestureControl</div>\n            </a>\n            <a href=\"#\" data-activates=\"mobile-demo\" class=\"button-collapse\"><i class=\"material-icons\">menu</i></a>\n\n            <ul id=\"nav-mobile\" class=\"right hide-on-med-and-down\">\n                <li><a [routerLink]=\"['/Devices']\" >Ger\u00E4te</a></li>\n                <li><a [routerLink]=\"['/Docs']\" >Handbuch</a></li>\n            </ul>\n\n            <ul class=\"side-nav\" id=\"mobile-demo\">\n                <li><a [routerLink]=\"['/Devices']\" >Ger\u00E4te</a></li>\n                <li><a [routerLink]=\"['/Docs']\" >Handbuch</a></li>\n            </ul>\n\n        </div>\n    </nav>\n</div>\n<main class=\"mdl-layout__content\">\n    <router-outlet ></router-outlet>\n</main>\n        "
	        }),
	        router_1.RouteConfig([
	            {
	                path: '/',
	                component: devicelist_component_ts_1.DevicesComponent,
	                name: 'Devices'
	            },
	            {
	                path: '/docs',
	                component: docs_component_1.DocsComponent,
	                name: 'Docs'
	            },
	            {
	                path: '/device/:id',
	                component: deviceDetail_component_1.DeviceDetailComponent,
	                name: 'DeviceDetail'
	            }
	        ]), 
	        __metadata('design:paramtypes', [router_1.Router])
	    ], App);
	    return App;
	}());
	exports.App = App;


/***/ },

/***/ 303:
/***/ function(module, exports) {

	/******/ (function(modules) { // webpackBootstrap
	/******/ 	// The module cache
	/******/ 	var installedModules = {};
	
	/******/ 	// The require function
	/******/ 	function __webpack_require__(moduleId) {
	
	/******/ 		// Check if module is in cache
	/******/ 		if(installedModules[moduleId])
	/******/ 			return installedModules[moduleId].exports;
	
	/******/ 		// Create a new module (and put it into the cache)
	/******/ 		var module = installedModules[moduleId] = {
	/******/ 			exports: {},
	/******/ 			id: moduleId,
	/******/ 			loaded: false
	/******/ 		};
	
	/******/ 		// Execute the module function
	/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
	
	/******/ 		// Flag the module as loaded
	/******/ 		module.loaded = true;
	
	/******/ 		// Return the exports of the module
	/******/ 		return module.exports;
	/******/ 	}
	
	
	/******/ 	// expose the modules object (__webpack_modules__)
	/******/ 	__webpack_require__.m = modules;
	
	/******/ 	// expose the module cache
	/******/ 	__webpack_require__.c = installedModules;
	
	/******/ 	// __webpack_public_path__
	/******/ 	__webpack_require__.p = "";
	
	/******/ 	// Load entry module and return exports
	/******/ 	return __webpack_require__(0);
	/******/ })
	/************************************************************************/
	/******/ ([
	/* 0 */
	/***/ function(module, exports, __webpack_require__) {
	
		/* WEBPACK VAR INJECTION */(function(global) {"use strict";
		__webpack_require__(1);
		var event_target_1 = __webpack_require__(2);
		var define_property_1 = __webpack_require__(4);
		var register_element_1 = __webpack_require__(5);
		var property_descriptor_1 = __webpack_require__(6);
		var utils_1 = __webpack_require__(3);
		var set = 'set';
		var clear = 'clear';
		var blockingMethods = ['alert', 'prompt', 'confirm'];
		var _global = typeof window == 'undefined' ? global : window;
		patchTimer(_global, set, clear, 'Timeout');
		patchTimer(_global, set, clear, 'Interval');
		patchTimer(_global, set, clear, 'Immediate');
		patchTimer(_global, 'request', 'cancelMacroTask', 'AnimationFrame');
		patchTimer(_global, 'mozRequest', 'mozCancel', 'AnimationFrame');
		patchTimer(_global, 'webkitRequest', 'webkitCancel', 'AnimationFrame');
		for (var i = 0; i < blockingMethods.length; i++) {
		    var name = blockingMethods[i];
		    utils_1.patchMethod(_global, name, function (delegate, symbol, name) {
		        return function (s, args) {
		            return Zone.current.run(delegate, _global, args, name);
		        };
		    });
		}
		event_target_1.eventTargetPatch(_global);
		property_descriptor_1.propertyDescriptorPatch(_global);
		utils_1.patchClass('MutationObserver');
		utils_1.patchClass('WebKitMutationObserver');
		utils_1.patchClass('FileReader');
		define_property_1.propertyPatch();
		register_element_1.registerElementPatch(_global);
		// Treat XMLHTTPRequest as a macrotask.
		patchXHR(_global);
		var XHR_TASK = utils_1.zoneSymbol('xhrTask');
		function patchXHR(window) {
		    function findPendingTask(target) {
		        var pendingTask = target[XHR_TASK];
		        return pendingTask;
		    }
		    function scheduleTask(task) {
		        var data = task.data;
		        data.target.addEventListener('readystatechange', function () {
		            if (data.target.readyState === XMLHttpRequest.DONE) {
		                if (!data.aborted) {
		                    task.invoke();
		                }
		            }
		        });
		        var storedTask = data.target[XHR_TASK];
		        if (!storedTask) {
		            data.target[XHR_TASK] = task;
		        }
		        setNative.apply(data.target, data.args);
		        return task;
		    }
		    function placeholderCallback() {
		    }
		    function clearTask(task) {
		        var data = task.data;
		        // Note - ideally, we would call data.target.removeEventListener here, but it's too late
		        // to prevent it from firing. So instead, we store info for the event listener.
		        data.aborted = true;
		        return clearNative.apply(data.target, data.args);
		    }
		    var setNative = utils_1.patchMethod(window.XMLHttpRequest.prototype, 'send', function () { return function (self, args) {
		        var zone = Zone.current;
		        var options = {
		            target: self,
		            isPeriodic: false,
		            delay: null,
		            args: args,
		            aborted: false
		        };
		        return zone.scheduleMacroTask('XMLHttpRequest.send', placeholderCallback, options, scheduleTask, clearTask);
		    }; });
		    var clearNative = utils_1.patchMethod(window.XMLHttpRequest.prototype, 'abort', function (delegate) { return function (self, args) {
		        var task = findPendingTask(self);
		        if (task && typeof task.type == 'string') {
		            // If the XHR has already completed, do nothing.
		            if (task.cancelFn == null) {
		                return;
		            }
		            task.zone.cancelTask(task);
		        }
		        // Otherwise, we are trying to abort an XHR which has not yet been sent, so there is no task to cancel. Do nothing.
		    }; });
		}
		/// GEO_LOCATION
		if (_global['navigator'] && _global['navigator'].geolocation) {
		    utils_1.patchPrototype(_global['navigator'].geolocation, [
		        'getCurrentPosition',
		        'watchPosition'
		    ]);
		}
		function patchTimer(window, setName, cancelName, nameSuffix) {
		    setName += nameSuffix;
		    cancelName += nameSuffix;
		    function scheduleTask(task) {
		        var data = task.data;
		        data.args[0] = task.invoke;
		        data.handleId = setNative.apply(window, data.args);
		        return task;
		    }
		    function clearTask(task) {
		        return clearNative(task.data.handleId);
		    }
		    var setNative = utils_1.patchMethod(window, setName, function (delegate) { return function (self, args) {
		        if (typeof args[0] === 'function') {
		            var zone = Zone.current;
		            var options = {
		                handleId: null,
		                isPeriodic: nameSuffix == 'Interval',
		                delay: (nameSuffix == 'Timeout' || nameSuffix == 'Interval') ? args[1] || 0 : null,
		                args: args
		            };
		            return zone.scheduleMacroTask(setName, args[0], options, scheduleTask, clearTask);
		        }
		        else {
		            // cause an error by calling it directly.
		            return delegate.apply(window, args);
		        }
		    }; });
		    var clearNative = utils_1.patchMethod(window, cancelName, function (delegate) { return function (self, args) {
		        var task = args[0];
		        if (task && typeof task.type == 'string') {
		            if (task.cancelFn && task.data.isPeriodic || task.runCount == 0) {
		                // Do not cancel already canceled functions
		                task.zone.cancelTask(task);
		            }
		        }
		        else {
		            // cause an error by calling it directly.
		            delegate.apply(window, args);
		        }
		    }; });
		}
	
		/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))
	
	/***/ },
	/* 1 */
	/***/ function(module, exports) {
	
		/* WEBPACK VAR INJECTION */(function(global) {;
		;
		var Zone = (function (global) {
		    var Zone = (function () {
		        function Zone(parent, zoneSpec) {
		            this._properties = null;
		            this._parent = parent;
		            this._name = zoneSpec ? zoneSpec.name || 'unnamed' : '<root>';
		            this._properties = zoneSpec && zoneSpec.properties || {};
		            this._zoneDelegate = new ZoneDelegate(this, this._parent && this._parent._zoneDelegate, zoneSpec);
		        }
		        Object.defineProperty(Zone, "current", {
		            get: function () { return _currentZone; },
		            enumerable: true,
		            configurable: true
		        });
		        ;
		        Object.defineProperty(Zone, "currentTask", {
		            get: function () { return _currentTask; },
		            enumerable: true,
		            configurable: true
		        });
		        ;
		        Object.defineProperty(Zone.prototype, "parent", {
		            get: function () { return this._parent; },
		            enumerable: true,
		            configurable: true
		        });
		        ;
		        Object.defineProperty(Zone.prototype, "name", {
		            get: function () { return this._name; },
		            enumerable: true,
		            configurable: true
		        });
		        ;
		        Zone.prototype.get = function (key) {
		            var current = this;
		            while (current) {
		                if (current._properties.hasOwnProperty(key)) {
		                    return current._properties[key];
		                }
		                current = current._parent;
		            }
		        };
		        Zone.prototype.fork = function (zoneSpec) {
		            if (!zoneSpec)
		                throw new Error('ZoneSpec required!');
		            return this._zoneDelegate.fork(this, zoneSpec);
		        };
		        Zone.prototype.wrap = function (callback, source) {
		            if (typeof callback != 'function') {
		                throw new Error('Expecting function got: ' + callback);
		            }
		            var callback = this._zoneDelegate.intercept(this, callback, source);
		            var zone = this;
		            return function () {
		                return zone.runGuarded(callback, this, arguments, source);
		            };
		        };
		        Zone.prototype.run = function (callback, applyThis, applyArgs, source) {
		            if (applyThis === void 0) { applyThis = null; }
		            if (applyArgs === void 0) { applyArgs = null; }
		            if (source === void 0) { source = null; }
		            var oldZone = _currentZone;
		            _currentZone = this;
		            try {
		                return this._zoneDelegate.invoke(this, callback, applyThis, applyArgs, source);
		            }
		            finally {
		                _currentZone = oldZone;
		            }
		        };
		        Zone.prototype.runGuarded = function (callback, applyThis, applyArgs, source) {
		            if (applyThis === void 0) { applyThis = null; }
		            if (applyArgs === void 0) { applyArgs = null; }
		            if (source === void 0) { source = null; }
		            var oldZone = _currentZone;
		            _currentZone = this;
		            try {
		                try {
		                    return this._zoneDelegate.invoke(this, callback, applyThis, applyArgs, source);
		                }
		                catch (error) {
		                    if (this._zoneDelegate.handleError(this, error)) {
		                        throw error;
		                    }
		                }
		            }
		            finally {
		                _currentZone = oldZone;
		            }
		        };
		        Zone.prototype.runTask = function (task, applyThis, applyArgs) {
		            task.runCount++;
		            if (task.zone != this)
		                throw new Error('A task can only be run in the zone which created it! (Creation: ' +
		                    task.zone.name + '; Execution: ' + this.name + ')');
		            var previousTask = _currentTask;
		            _currentTask = task;
		            var oldZone = _currentZone;
		            _currentZone = this;
		            try {
		                try {
		                    return this._zoneDelegate.invokeTask(this, task, applyThis, applyArgs);
		                }
		                catch (error) {
		                    if (this._zoneDelegate.handleError(this, error)) {
		                        throw error;
		                    }
		                }
		            }
		            finally {
		                if (task.type == 'macroTask' && task.data && !task.data.isPeriodic) {
		                    task.cancelFn = null;
		                }
		                _currentZone = oldZone;
		                _currentTask = previousTask;
		            }
		        };
		        Zone.prototype.scheduleMicroTask = function (source, callback, data, customSchedule) {
		            return this._zoneDelegate.scheduleTask(this, new ZoneTask('microTask', this, source, callback, data, customSchedule, null));
		        };
		        Zone.prototype.scheduleMacroTask = function (source, callback, data, customSchedule, customCancel) {
		            return this._zoneDelegate.scheduleTask(this, new ZoneTask('macroTask', this, source, callback, data, customSchedule, customCancel));
		        };
		        Zone.prototype.scheduleEventTask = function (source, callback, data, customSchedule, customCancel) {
		            return this._zoneDelegate.scheduleTask(this, new ZoneTask('eventTask', this, source, callback, data, customSchedule, customCancel));
		        };
		        Zone.prototype.cancelTask = function (task) {
		            var value = this._zoneDelegate.cancelTask(this, task);
		            task.runCount = -1;
		            task.cancelFn = null;
		            return value;
		        };
		        Zone.__symbol__ = __symbol__;
		        return Zone;
		    }());
		    ;
		    var ZoneDelegate = (function () {
		        function ZoneDelegate(zone, parentDelegate, zoneSpec) {
		            this._taskCounts = { microTask: 0, macroTask: 0, eventTask: 0 };
		            this.zone = zone;
		            this._parentDelegate = parentDelegate;
		            this._forkZS = zoneSpec && (zoneSpec && zoneSpec.onFork ? zoneSpec : parentDelegate._forkZS);
		            this._forkDlgt = zoneSpec && (zoneSpec.onFork ? parentDelegate : parentDelegate._forkDlgt);
		            this._interceptZS = zoneSpec && (zoneSpec.onIntercept ? zoneSpec : parentDelegate._interceptZS);
		            this._interceptDlgt = zoneSpec && (zoneSpec.onIntercept ? parentDelegate : parentDelegate._interceptDlgt);
		            this._invokeZS = zoneSpec && (zoneSpec.onInvoke ? zoneSpec : parentDelegate._invokeZS);
		            this._invokeDlgt = zoneSpec && (zoneSpec.onInvoke ? parentDelegate : parentDelegate._invokeDlgt);
		            this._handleErrorZS = zoneSpec && (zoneSpec.onHandleError ? zoneSpec : parentDelegate._handleErrorZS);
		            this._handleErrorDlgt = zoneSpec && (zoneSpec.onHandleError ? parentDelegate : parentDelegate._handleErrorDlgt);
		            this._scheduleTaskZS = zoneSpec && (zoneSpec.onScheduleTask ? zoneSpec : parentDelegate._scheduleTaskZS);
		            this._scheduleTaskDlgt = zoneSpec && (zoneSpec.onScheduleTask ? parentDelegate : parentDelegate._scheduleTaskDlgt);
		            this._invokeTaskZS = zoneSpec && (zoneSpec.onInvokeTask ? zoneSpec : parentDelegate._invokeTaskZS);
		            this._invokeTaskDlgt = zoneSpec && (zoneSpec.onInvokeTask ? parentDelegate : parentDelegate._invokeTaskDlgt);
		            this._cancelTaskZS = zoneSpec && (zoneSpec.onCancelTask ? zoneSpec : parentDelegate._cancelTaskZS);
		            this._cancelTaskDlgt = zoneSpec && (zoneSpec.onCancelTask ? parentDelegate : parentDelegate._cancelTaskDlgt);
		            this._hasTaskZS = zoneSpec && (zoneSpec.onHasTask ? zoneSpec : parentDelegate._hasTaskZS);
		            this._hasTaskDlgt = zoneSpec && (zoneSpec.onHasTask ? parentDelegate : parentDelegate._hasTaskDlgt);
		        }
		        ZoneDelegate.prototype.fork = function (targetZone, zoneSpec) {
		            return this._forkZS
		                ? this._forkZS.onFork(this._forkDlgt, this.zone, targetZone, zoneSpec)
		                : new Zone(targetZone, zoneSpec);
		        };
		        ZoneDelegate.prototype.intercept = function (targetZone, callback, source) {
		            return this._interceptZS
		                ? this._interceptZS.onIntercept(this._interceptDlgt, this.zone, targetZone, callback, source)
		                : callback;
		        };
		        ZoneDelegate.prototype.invoke = function (targetZone, callback, applyThis, applyArgs, source) {
		            return this._invokeZS
		                ? this._invokeZS.onInvoke(this._invokeDlgt, this.zone, targetZone, callback, applyThis, applyArgs, source)
		                : callback.apply(applyThis, applyArgs);
		        };
		        ZoneDelegate.prototype.handleError = function (targetZone, error) {
		            return this._handleErrorZS
		                ? this._handleErrorZS.onHandleError(this._handleErrorDlgt, this.zone, targetZone, error)
		                : true;
		        };
		        ZoneDelegate.prototype.scheduleTask = function (targetZone, task) {
		            try {
		                if (this._scheduleTaskZS) {
		                    return this._scheduleTaskZS.onScheduleTask(this._scheduleTaskDlgt, this.zone, targetZone, task);
		                }
		                else if (task.scheduleFn) {
		                    task.scheduleFn(task);
		                }
		                else if (task.type == 'microTask') {
		                    scheduleMicroTask(task);
		                }
		                else {
		                    throw new Error('Task is missing scheduleFn.');
		                }
		                return task;
		            }
		            finally {
		                if (targetZone == this.zone) {
		                    this._updateTaskCount(task.type, 1);
		                }
		            }
		        };
		        ZoneDelegate.prototype.invokeTask = function (targetZone, task, applyThis, applyArgs) {
		            try {
		                return this._invokeTaskZS
		                    ? this._invokeTaskZS.onInvokeTask(this._invokeTaskDlgt, this.zone, targetZone, task, applyThis, applyArgs)
		                    : task.callback.apply(applyThis, applyArgs);
		            }
		            finally {
		                if (targetZone == this.zone && (task.type != 'eventTask') && !(task.data && task.data.isPeriodic)) {
		                    this._updateTaskCount(task.type, -1);
		                }
		            }
		        };
		        ZoneDelegate.prototype.cancelTask = function (targetZone, task) {
		            var value;
		            if (this._cancelTaskZS) {
		                value = this._cancelTaskZS.onCancelTask(this._cancelTaskDlgt, this.zone, targetZone, task);
		            }
		            else if (!task.cancelFn) {
		                throw new Error('Task does not support cancellation, or is already canceled.');
		            }
		            else {
		                value = task.cancelFn(task);
		            }
		            if (targetZone == this.zone) {
		                // this should not be in the finally block, because exceptions assume not canceled.
		                this._updateTaskCount(task.type, -1);
		            }
		            return value;
		        };
		        ZoneDelegate.prototype.hasTask = function (targetZone, isEmpty) {
		            return this._hasTaskZS && this._hasTaskZS.onHasTask(this._hasTaskDlgt, this.zone, targetZone, isEmpty);
		        };
		        ZoneDelegate.prototype._updateTaskCount = function (type, count) {
		            var counts = this._taskCounts;
		            var prev = counts[type];
		            var next = counts[type] = prev + count;
		            if (next < 0) {
		                throw new Error('More tasks executed then were scheduled.');
		            }
		            if (prev == 0 || next == 0) {
		                var isEmpty = {
		                    microTask: counts.microTask > 0,
		                    macroTask: counts.macroTask > 0,
		                    eventTask: counts.eventTask > 0,
		                    change: type
		                };
		                try {
		                    this.hasTask(this.zone, isEmpty);
		                }
		                finally {
		                    if (this._parentDelegate) {
		                        this._parentDelegate._updateTaskCount(type, count);
		                    }
		                }
		            }
		        };
		        return ZoneDelegate;
		    }());
		    var ZoneTask = (function () {
		        function ZoneTask(type, zone, source, callback, options, scheduleFn, cancelFn) {
		            this.runCount = 0;
		            this.type = type;
		            this.zone = zone;
		            this.source = source;
		            this.data = options;
		            this.scheduleFn = scheduleFn;
		            this.cancelFn = cancelFn;
		            this.callback = callback;
		            var self = this;
		            this.invoke = function () {
		                try {
		                    return zone.runTask(self, this, arguments);
		                }
		                finally {
		                    drainMicroTaskQueue();
		                }
		            };
		        }
		        return ZoneTask;
		    }());
		    function __symbol__(name) { return '__zone_symbol__' + name; }
		    ;
		    var symbolSetTimeout = __symbol__('setTimeout');
		    var symbolPromise = __symbol__('Promise');
		    var symbolThen = __symbol__('then');
		    var _currentZone = new Zone(null, null);
		    var _currentTask = null;
		    var _microTaskQueue = [];
		    var _isDrainingMicrotaskQueue = false;
		    var _uncaughtPromiseErrors = [];
		    var _drainScheduled = false;
		    function scheduleQueueDrain() {
		        if (!_drainScheduled && !_currentTask && _microTaskQueue.length == 0) {
		            // We are not running in Task, so we need to kickstart the microtask queue.
		            if (global[symbolPromise]) {
		                global[symbolPromise].resolve(0)[symbolThen](drainMicroTaskQueue);
		            }
		            else {
		                global[symbolSetTimeout](drainMicroTaskQueue, 0);
		            }
		        }
		    }
		    function scheduleMicroTask(task) {
		        scheduleQueueDrain();
		        _microTaskQueue.push(task);
		    }
		    function consoleError(e) {
		        var rejection = e && e.rejection;
		        if (rejection) {
		            console.error('Unhandled Promise rejection:', rejection instanceof Error ? rejection.message : rejection, '; Zone:', e.zone.name, '; Task:', e.task && e.task.source, '; Value:', rejection);
		        }
		        console.error(e);
		    }
		    function drainMicroTaskQueue() {
		        if (!_isDrainingMicrotaskQueue) {
		            _isDrainingMicrotaskQueue = true;
		            while (_microTaskQueue.length) {
		                var queue = _microTaskQueue;
		                _microTaskQueue = [];
		                for (var i = 0; i < queue.length; i++) {
		                    var task = queue[i];
		                    try {
		                        task.zone.runTask(task, null, null);
		                    }
		                    catch (e) {
		                        consoleError(e);
		                    }
		                }
		            }
		            while (_uncaughtPromiseErrors.length) {
		                var uncaughtPromiseErrors = _uncaughtPromiseErrors;
		                _uncaughtPromiseErrors = [];
		                for (var i = 0; i < uncaughtPromiseErrors.length; i++) {
		                    var uncaughtPromiseError = uncaughtPromiseErrors[i];
		                    try {
		                        uncaughtPromiseError.zone.runGuarded(function () { throw uncaughtPromiseError; });
		                    }
		                    catch (e) {
		                        consoleError(e);
		                    }
		                }
		            }
		            _isDrainingMicrotaskQueue = false;
		            _drainScheduled = false;
		        }
		    }
		    function isThenable(value) {
		        return value && value.then;
		    }
		    function forwardResolution(value) { return value; }
		    function forwardRejection(rejection) { return ZoneAwarePromise.reject(rejection); }
		    var symbolState = __symbol__('state');
		    var symbolValue = __symbol__('value');
		    var source = 'Promise.then';
		    var UNRESOLVED = null;
		    var RESOLVED = true;
		    var REJECTED = false;
		    var REJECTED_NO_CATCH = 0;
		    function makeResolver(promise, state) {
		        return function (v) {
		            resolvePromise(promise, state, v);
		            // Do not return value or you will break the Promise spec.
		        };
		    }
		    function resolvePromise(promise, state, value) {
		        if (promise[symbolState] === UNRESOLVED) {
		            if (value instanceof ZoneAwarePromise && value[symbolState] !== UNRESOLVED) {
		                clearRejectedNoCatch(value);
		                resolvePromise(promise, value[symbolState], value[symbolValue]);
		            }
		            else if (isThenable(value)) {
		                value.then(makeResolver(promise, state), makeResolver(promise, false));
		            }
		            else {
		                promise[symbolState] = state;
		                var queue = promise[symbolValue];
		                promise[symbolValue] = value;
		                for (var i = 0; i < queue.length;) {
		                    scheduleResolveOrReject(promise, queue[i++], queue[i++], queue[i++], queue[i++]);
		                }
		                if (queue.length == 0 && state == REJECTED) {
		                    promise[symbolState] = REJECTED_NO_CATCH;
		                    try {
		                        throw new Error("Uncaught (in promise): " + value);
		                    }
		                    catch (e) {
		                        var error = e;
		                        error.rejection = value;
		                        error.promise = promise;
		                        error.zone = Zone.current;
		                        error.task = Zone.currentTask;
		                        _uncaughtPromiseErrors.push(error);
		                        scheduleQueueDrain();
		                    }
		                }
		            }
		        }
		        // Resolving an already resolved promise is a noop.
		        return promise;
		    }
		    function clearRejectedNoCatch(promise) {
		        if (promise[symbolState] === REJECTED_NO_CATCH) {
		            promise[symbolState] = REJECTED;
		            for (var i = 0; i < _uncaughtPromiseErrors.length; i++) {
		                if (promise === _uncaughtPromiseErrors[i].promise) {
		                    _uncaughtPromiseErrors.splice(i, 1);
		                    break;
		                }
		            }
		        }
		    }
		    function scheduleResolveOrReject(promise, zone, chainPromise, onFulfilled, onRejected) {
		        clearRejectedNoCatch(promise);
		        var delegate = promise[symbolState] ? onFulfilled || forwardResolution : onRejected || forwardRejection;
		        zone.scheduleMicroTask(source, function () {
		            try {
		                resolvePromise(chainPromise, true, zone.run(delegate, null, [promise[symbolValue]]));
		            }
		            catch (error) {
		                resolvePromise(chainPromise, false, error);
		            }
		        });
		    }
		    var ZoneAwarePromise = (function () {
		        function ZoneAwarePromise(executor) {
		            var promise = this;
		            promise[symbolState] = UNRESOLVED;
		            promise[symbolValue] = []; // queue;
		            try {
		                executor && executor(makeResolver(promise, RESOLVED), makeResolver(promise, REJECTED));
		            }
		            catch (e) {
		                resolvePromise(promise, false, e);
		            }
		        }
		        ZoneAwarePromise.resolve = function (value) {
		            return resolvePromise(new this(null), RESOLVED, value);
		        };
		        ZoneAwarePromise.reject = function (error) {
		            return resolvePromise(new this(null), REJECTED, error);
		        };
		        ZoneAwarePromise.race = function (values) {
		            var resolve;
		            var reject;
		            var promise = new this(function (res, rej) { resolve = res; reject = rej; });
		            function onResolve(value) { promise && (promise = null || resolve(value)); }
		            function onReject(error) { promise && (promise = null || reject(error)); }
		            for (var _i = 0, values_1 = values; _i < values_1.length; _i++) {
		                var value = values_1[_i];
		                if (!isThenable(value)) {
		                    value = this.resolve(value);
		                }
		                value.then(onResolve, onReject);
		            }
		            return promise;
		        };
		        ZoneAwarePromise.all = function (values) {
		            var resolve;
		            var reject;
		            var promise = new this(function (res, rej) { resolve = res; reject = rej; });
		            var resolvedValues = [];
		            var count = 0;
		            function onReject(error) { promise && reject(error); promise = null; }
		            for (var _i = 0, values_2 = values; _i < values_2.length; _i++) {
		                var value = values_2[_i];
		                if (!isThenable(value)) {
		                    value = this.resolve(value);
		                }
		                value.then((function (index) { return function (value) {
		                    resolvedValues[index] = value;
		                    count--;
		                    if (promise && !count) {
		                        resolve(resolvedValues);
		                    }
		                    promise == null;
		                }; })(count), onReject);
		                count++;
		            }
		            if (!count)
		                resolve(resolvedValues);
		            return promise;
		        };
		        ZoneAwarePromise.prototype.then = function (onFulfilled, onRejected) {
		            var chainPromise = new ZoneAwarePromise(null);
		            var zone = Zone.current;
		            if (this[symbolState] == UNRESOLVED) {
		                this[symbolValue].push(zone, chainPromise, onFulfilled, onRejected);
		            }
		            else {
		                scheduleResolveOrReject(this, zone, chainPromise, onFulfilled, onRejected);
		            }
		            return chainPromise;
		        };
		        ZoneAwarePromise.prototype.catch = function (onRejected) {
		            return this.then(null, onRejected);
		        };
		        return ZoneAwarePromise;
		    }());
		    var NativePromise = global[__symbol__('Promise')] = global.Promise;
		    global.Promise = ZoneAwarePromise;
		    if (NativePromise) {
		        var NativePromiseProtototype = NativePromise.prototype;
		        var NativePromiseThen = NativePromiseProtototype[__symbol__('then')]
		            = NativePromiseProtototype.then;
		        NativePromiseProtototype.then = function (onResolve, onReject) {
		            var nativePromise = this;
		            return new ZoneAwarePromise(function (resolve, reject) {
		                NativePromiseThen.call(nativePromise, resolve, reject);
		            }).then(onResolve, onReject);
		        };
		    }
		    return global.Zone = Zone;
		})(typeof window == 'undefined' ? global : window);
	
		/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))
	
	/***/ },
	/* 2 */
	/***/ function(module, exports, __webpack_require__) {
	
		"use strict";
		var utils_1 = __webpack_require__(3);
		var WTF_ISSUE_555 = 'Anchor,Area,Audio,BR,Base,BaseFont,Body,Button,Canvas,Content,DList,Directory,Div,Embed,FieldSet,Font,Form,Frame,FrameSet,HR,Head,Heading,Html,IFrame,Image,Input,Keygen,LI,Label,Legend,Link,Map,Marquee,Media,Menu,Meta,Meter,Mod,OList,Object,OptGroup,Option,Output,Paragraph,Pre,Progress,Quote,Script,Select,Source,Span,Style,TableCaption,TableCell,TableCol,Table,TableRow,TableSection,TextArea,Title,Track,UList,Unknown,Video';
		var NO_EVENT_TARGET = 'ApplicationCache,EventSource,FileReader,InputMethodContext,MediaController,MessagePort,Node,Performance,SVGElementInstance,SharedWorker,TextTrack,TextTrackCue,TextTrackList,WebKitNamedFlow,Worker,WorkerGlobalScope,XMLHttpRequest,XMLHttpRequestEventTarget,XMLHttpRequestUpload,IDBRequest,IDBOpenDBRequest,IDBDatabase,IDBTransaction,IDBCursor,DBIndex'.split(',');
		var EVENT_TARGET = 'EventTarget';
		function eventTargetPatch(_global) {
		    var apis = [];
		    var isWtf = _global['wtf'];
		    if (isWtf) {
		        // Workaround for: https://github.com/google/tracing-framework/issues/555
		        apis = WTF_ISSUE_555.split(',').map(function (v) { return 'HTML' + v + 'Element'; }).concat(NO_EVENT_TARGET);
		    }
		    else if (_global[EVENT_TARGET]) {
		        apis.push(EVENT_TARGET);
		    }
		    else {
		        // Note: EventTarget is not available in all browsers,
		        // if it's not available, we instead patch the APIs in the IDL that inherit from EventTarget
		        apis = NO_EVENT_TARGET;
		    }
		    for (var i = 0; i < apis.length; i++) {
		        var type = _global[apis[i]];
		        utils_1.patchEventTargetMethods(type && type.prototype);
		    }
		}
		exports.eventTargetPatch = eventTargetPatch;
	
	
	/***/ },
	/* 3 */
	/***/ function(module, exports) {
	
		/* WEBPACK VAR INJECTION */(function(global) {/**
		 * Suppress closure compiler errors about unknown 'process' variable
		 * @fileoverview
		 * @suppress {undefinedVars}
		 */
		"use strict";
		exports.zoneSymbol = Zone['__symbol__'];
		var _global = typeof window == 'undefined' ? global : window;
		function bindArguments(args, source) {
		    for (var i = args.length - 1; i >= 0; i--) {
		        if (typeof args[i] === 'function') {
		            args[i] = Zone.current.wrap(args[i], source + '_' + i);
		        }
		    }
		    return args;
		}
		exports.bindArguments = bindArguments;
		;
		function patchPrototype(prototype, fnNames) {
		    var source = prototype.constructor['name'];
		    for (var i = 0; i < fnNames.length; i++) {
		        var name = fnNames[i];
		        var delegate = prototype[name];
		        if (delegate) {
		            prototype[name] = (function (delegate) {
		                return function () {
		                    return delegate.apply(this, bindArguments(arguments, source + '.' + name));
		                };
		            })(delegate);
		        }
		    }
		}
		exports.patchPrototype = patchPrototype;
		;
		exports.isWebWorker = (typeof WorkerGlobalScope !== 'undefined' && self instanceof WorkerGlobalScope);
		exports.isNode = (typeof process !== 'undefined' && {}.toString.call(process) === '[object process]');
		exports.isBrowser = !exports.isNode && !exports.isWebWorker && !!(window && window['HTMLElement']);
		function patchProperty(obj, prop) {
		    var desc = Object.getOwnPropertyDescriptor(obj, prop) || {
		        enumerable: true,
		        configurable: true
		    };
		    // A property descriptor cannot have getter/setter and be writable
		    // deleting the writable and value properties avoids this error:
		    //
		    // TypeError: property descriptors must not specify a value or be writable when a
		    // getter or setter has been specified
		    delete desc.writable;
		    delete desc.value;
		    // substr(2) cuz 'onclick' -> 'click', etc
		    var eventName = prop.substr(2);
		    var _prop = '_' + prop;
		    desc.set = function (fn) {
		        if (this[_prop]) {
		            this.removeEventListener(eventName, this[_prop]);
		        }
		        if (typeof fn === 'function') {
		            var wrapFn = function (event) {
		                var result;
		                result = fn.apply(this, arguments);
		                if (result != undefined && !result)
		                    event.preventDefault();
		            };
		            this[_prop] = wrapFn;
		            this.addEventListener(eventName, wrapFn, false);
		        }
		        else {
		            this[_prop] = null;
		        }
		    };
		    desc.get = function () {
		        return this[_prop];
		    };
		    Object.defineProperty(obj, prop, desc);
		}
		exports.patchProperty = patchProperty;
		;
		function patchOnProperties(obj, properties) {
		    var onProperties = [];
		    for (var prop in obj) {
		        if (prop.substr(0, 2) == 'on') {
		            onProperties.push(prop);
		        }
		    }
		    for (var j = 0; j < onProperties.length; j++) {
		        patchProperty(obj, onProperties[j]);
		    }
		    if (properties) {
		        for (var i = 0; i < properties.length; i++) {
		            patchProperty(obj, 'on' + properties[i]);
		        }
		    }
		}
		exports.patchOnProperties = patchOnProperties;
		;
		var EVENT_TASKS = exports.zoneSymbol('eventTasks');
		var ADD_EVENT_LISTENER = 'addEventListener';
		var REMOVE_EVENT_LISTENER = 'removeEventListener';
		var SYMBOL_ADD_EVENT_LISTENER = exports.zoneSymbol(ADD_EVENT_LISTENER);
		var SYMBOL_REMOVE_EVENT_LISTENER = exports.zoneSymbol(REMOVE_EVENT_LISTENER);
		function findExistingRegisteredTask(target, handler, name, capture, remove) {
		    var eventTasks = target[EVENT_TASKS];
		    if (eventTasks) {
		        for (var i = 0; i < eventTasks.length; i++) {
		            var eventTask = eventTasks[i];
		            var data = eventTask.data;
		            if (data.handler === handler
		                && data.useCapturing === capture
		                && data.eventName === name) {
		                if (remove) {
		                    eventTasks.splice(i, 1);
		                }
		                return eventTask;
		            }
		        }
		    }
		    return null;
		}
		function attachRegisteredEvent(target, eventTask) {
		    var eventTasks = target[EVENT_TASKS];
		    if (!eventTasks) {
		        eventTasks = target[EVENT_TASKS] = [];
		    }
		    eventTasks.push(eventTask);
		}
		function scheduleEventListener(eventTask) {
		    var meta = eventTask.data;
		    attachRegisteredEvent(meta.target, eventTask);
		    return meta.target[SYMBOL_ADD_EVENT_LISTENER](meta.eventName, eventTask.invoke, meta.useCapturing);
		}
		function cancelEventListener(eventTask) {
		    var meta = eventTask.data;
		    findExistingRegisteredTask(meta.target, eventTask.invoke, meta.eventName, meta.useCapturing, true);
		    meta.target[SYMBOL_REMOVE_EVENT_LISTENER](meta.eventName, eventTask.invoke, meta.useCapturing);
		}
		function zoneAwareAddEventListener(self, args) {
		    var eventName = args[0];
		    var handler = args[1];
		    var useCapturing = args[2] || false;
		    // - Inside a Web Worker, `this` is undefined, the context is `global`
		    // - When `addEventListener` is called on the global context in strict mode, `this` is undefined
		    // see https://github.com/angular/zone.js/issues/190
		    var target = self || _global;
		    var delegate = null;
		    if (typeof handler == 'function') {
		        delegate = handler;
		    }
		    else if (handler && handler.handleEvent) {
		        delegate = function (event) { return handler.handleEvent(event); };
		    }
		    // Ignore special listeners of IE11 & Edge dev tools, see https://github.com/angular/zone.js/issues/150
		    if (!delegate || handler && handler.toString() === "[object FunctionWrapper]") {
		        return target[SYMBOL_ADD_EVENT_LISTENER](eventName, handler, useCapturing);
		    }
		    var eventTask = findExistingRegisteredTask(target, handler, eventName, useCapturing, false);
		    if (eventTask) {
		        // we already registered, so this will have noop.
		        return target[SYMBOL_ADD_EVENT_LISTENER](eventName, eventTask.invoke, useCapturing);
		    }
		    var zone = Zone.current;
		    var source = target.constructor['name'] + '.addEventListener:' + eventName;
		    var data = {
		        target: target,
		        eventName: eventName,
		        name: eventName,
		        useCapturing: useCapturing,
		        handler: handler
		    };
		    zone.scheduleEventTask(source, delegate, data, scheduleEventListener, cancelEventListener);
		}
		function zoneAwareRemoveEventListener(self, args) {
		    var eventName = args[0];
		    var handler = args[1];
		    var useCapturing = args[2] || false;
		    // - Inside a Web Worker, `this` is undefined, the context is `global`
		    // - When `addEventListener` is called on the global context in strict mode, `this` is undefined
		    // see https://github.com/angular/zone.js/issues/190
		    var target = self || _global;
		    var eventTask = findExistingRegisteredTask(target, handler, eventName, useCapturing, true);
		    if (eventTask) {
		        eventTask.zone.cancelTask(eventTask);
		    }
		    else {
		        target[SYMBOL_REMOVE_EVENT_LISTENER](eventName, handler, useCapturing);
		    }
		}
		function patchEventTargetMethods(obj) {
		    if (obj && obj.addEventListener) {
		        patchMethod(obj, ADD_EVENT_LISTENER, function () { return zoneAwareAddEventListener; });
		        patchMethod(obj, REMOVE_EVENT_LISTENER, function () { return zoneAwareRemoveEventListener; });
		        return true;
		    }
		    else {
		        return false;
		    }
		}
		exports.patchEventTargetMethods = patchEventTargetMethods;
		;
		var originalInstanceKey = exports.zoneSymbol('originalInstance');
		// wrap some native API on `window`
		function patchClass(className) {
		    var OriginalClass = _global[className];
		    if (!OriginalClass)
		        return;
		    _global[className] = function () {
		        var a = bindArguments(arguments, className);
		        switch (a.length) {
		            case 0:
		                this[originalInstanceKey] = new OriginalClass();
		                break;
		            case 1:
		                this[originalInstanceKey] = new OriginalClass(a[0]);
		                break;
		            case 2:
		                this[originalInstanceKey] = new OriginalClass(a[0], a[1]);
		                break;
		            case 3:
		                this[originalInstanceKey] = new OriginalClass(a[0], a[1], a[2]);
		                break;
		            case 4:
		                this[originalInstanceKey] = new OriginalClass(a[0], a[1], a[2], a[3]);
		                break;
		            default: throw new Error('Arg list too long.');
		        }
		    };
		    var instance = new OriginalClass(function () { });
		    var prop;
		    for (prop in instance) {
		        (function (prop) {
		            if (typeof instance[prop] === 'function') {
		                _global[className].prototype[prop] = function () {
		                    return this[originalInstanceKey][prop].apply(this[originalInstanceKey], arguments);
		                };
		            }
		            else {
		                Object.defineProperty(_global[className].prototype, prop, {
		                    set: function (fn) {
		                        if (typeof fn === 'function') {
		                            this[originalInstanceKey][prop] = Zone.current.wrap(fn, className + '.' + prop);
		                        }
		                        else {
		                            this[originalInstanceKey][prop] = fn;
		                        }
		                    },
		                    get: function () {
		                        return this[originalInstanceKey][prop];
		                    }
		                });
		            }
		        }(prop));
		    }
		    for (prop in OriginalClass) {
		        if (prop !== 'prototype' && OriginalClass.hasOwnProperty(prop)) {
		            _global[className][prop] = OriginalClass[prop];
		        }
		    }
		}
		exports.patchClass = patchClass;
		;
		function createNamedFn(name, delegate) {
		    try {
		        return (Function('f', "return function " + name + "(){return f(this, arguments)}"))(delegate);
		    }
		    catch (e) {
		        // if we fail, we must be CSP, just return delegate.
		        return function () {
		            return delegate(this, arguments);
		        };
		    }
		}
		exports.createNamedFn = createNamedFn;
		function patchMethod(target, name, patchFn) {
		    var proto = target;
		    while (proto && !proto.hasOwnProperty(name)) {
		        proto = Object.getPrototypeOf(proto);
		    }
		    if (!proto && target[name]) {
		        // somehow we did not find it, but we can see it. This happens on IE for Window properties.
		        proto = target;
		    }
		    var delegateName = exports.zoneSymbol(name);
		    var delegate;
		    if (proto && !(delegate = proto[delegateName])) {
		        delegate = proto[delegateName] = proto[name];
		        proto[name] = createNamedFn(name, patchFn(delegate, delegateName, name));
		    }
		    return delegate;
		}
		exports.patchMethod = patchMethod;
	
		/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))
	
	/***/ },
	/* 4 */
	/***/ function(module, exports, __webpack_require__) {
	
		"use strict";
		var utils_1 = __webpack_require__(3);
		// might need similar for object.freeze
		// i regret nothing
		var _defineProperty = Object.defineProperty;
		var _getOwnPropertyDescriptor = Object.getOwnPropertyDescriptor;
		var _create = Object.create;
		var unconfigurablesKey = utils_1.zoneSymbol('unconfigurables');
		function propertyPatch() {
		    Object.defineProperty = function (obj, prop, desc) {
		        if (isUnconfigurable(obj, prop)) {
		            throw new TypeError('Cannot assign to read only property \'' + prop + '\' of ' + obj);
		        }
		        if (prop !== 'prototype') {
		            desc = rewriteDescriptor(obj, prop, desc);
		        }
		        return _defineProperty(obj, prop, desc);
		    };
		    Object.defineProperties = function (obj, props) {
		        Object.keys(props).forEach(function (prop) {
		            Object.defineProperty(obj, prop, props[prop]);
		        });
		        return obj;
		    };
		    Object.create = function (obj, proto) {
		        if (typeof proto === 'object') {
		            Object.keys(proto).forEach(function (prop) {
		                proto[prop] = rewriteDescriptor(obj, prop, proto[prop]);
		            });
		        }
		        return _create(obj, proto);
		    };
		    Object.getOwnPropertyDescriptor = function (obj, prop) {
		        var desc = _getOwnPropertyDescriptor(obj, prop);
		        if (isUnconfigurable(obj, prop)) {
		            desc.configurable = false;
		        }
		        return desc;
		    };
		}
		exports.propertyPatch = propertyPatch;
		;
		function _redefineProperty(obj, prop, desc) {
		    desc = rewriteDescriptor(obj, prop, desc);
		    return _defineProperty(obj, prop, desc);
		}
		exports._redefineProperty = _redefineProperty;
		;
		function isUnconfigurable(obj, prop) {
		    return obj && obj[unconfigurablesKey] && obj[unconfigurablesKey][prop];
		}
		function rewriteDescriptor(obj, prop, desc) {
		    desc.configurable = true;
		    if (!desc.configurable) {
		        if (!obj[unconfigurablesKey]) {
		            _defineProperty(obj, unconfigurablesKey, { writable: true, value: {} });
		        }
		        obj[unconfigurablesKey][prop] = true;
		    }
		    return desc;
		}
	
	
	/***/ },
	/* 5 */
	/***/ function(module, exports, __webpack_require__) {
	
		"use strict";
		var define_property_1 = __webpack_require__(4);
		var utils_1 = __webpack_require__(3);
		function registerElementPatch(_global) {
		    if (!utils_1.isBrowser || !('registerElement' in _global.document)) {
		        return;
		    }
		    var _registerElement = document.registerElement;
		    var callbacks = [
		        'createdCallback',
		        'attachedCallback',
		        'detachedCallback',
		        'attributeChangedCallback'
		    ];
		    document.registerElement = function (name, opts) {
		        if (opts && opts.prototype) {
		            callbacks.forEach(function (callback) {
		                var source = 'Document.registerElement::' + callback;
		                if (opts.prototype.hasOwnProperty(callback)) {
		                    var descriptor = Object.getOwnPropertyDescriptor(opts.prototype, callback);
		                    if (descriptor && descriptor.value) {
		                        descriptor.value = Zone.current.wrap(descriptor.value, source);
		                        define_property_1._redefineProperty(opts.prototype, callback, descriptor);
		                    }
		                    else {
		                        opts.prototype[callback] = Zone.current.wrap(opts.prototype[callback], source);
		                    }
		                }
		                else if (opts.prototype[callback]) {
		                    opts.prototype[callback] = Zone.current.wrap(opts.prototype[callback], source);
		                }
		            });
		        }
		        return _registerElement.apply(document, [name, opts]);
		    };
		}
		exports.registerElementPatch = registerElementPatch;
	
	
	/***/ },
	/* 6 */
	/***/ function(module, exports, __webpack_require__) {
	
		"use strict";
		var webSocketPatch = __webpack_require__(7);
		var utils_1 = __webpack_require__(3);
		var eventNames = 'copy cut paste abort blur focus canplay canplaythrough change click contextmenu dblclick drag dragend dragenter dragleave dragover dragstart drop durationchange emptied ended input invalid keydown keypress keyup load loadeddata loadedmetadata loadstart message mousedown mouseenter mouseleave mousemove mouseout mouseover mouseup pause play playing progress ratechange reset scroll seeked seeking select show stalled submit suspend timeupdate volumechange waiting mozfullscreenchange mozfullscreenerror mozpointerlockchange mozpointerlockerror error webglcontextrestored webglcontextlost webglcontextcreationerror'.split(' ');
		function propertyDescriptorPatch(_global) {
		    if (utils_1.isNode) {
		        return;
		    }
		    var supportsWebSocket = typeof WebSocket !== 'undefined';
		    if (canPatchViaPropertyDescriptor()) {
		        // for browsers that we can patch the descriptor:  Chrome & Firefox
		        if (utils_1.isBrowser) {
		            utils_1.patchOnProperties(HTMLElement.prototype, eventNames);
		        }
		        utils_1.patchOnProperties(XMLHttpRequest.prototype, null);
		        if (typeof IDBIndex !== 'undefined') {
		            utils_1.patchOnProperties(IDBIndex.prototype, null);
		            utils_1.patchOnProperties(IDBRequest.prototype, null);
		            utils_1.patchOnProperties(IDBOpenDBRequest.prototype, null);
		            utils_1.patchOnProperties(IDBDatabase.prototype, null);
		            utils_1.patchOnProperties(IDBTransaction.prototype, null);
		            utils_1.patchOnProperties(IDBCursor.prototype, null);
		        }
		        if (supportsWebSocket) {
		            utils_1.patchOnProperties(WebSocket.prototype, null);
		        }
		    }
		    else {
		        // Safari, Android browsers (Jelly Bean)
		        patchViaCapturingAllTheEvents();
		        utils_1.patchClass('XMLHttpRequest');
		        if (supportsWebSocket) {
		            webSocketPatch.apply(_global);
		        }
		    }
		}
		exports.propertyDescriptorPatch = propertyDescriptorPatch;
		function canPatchViaPropertyDescriptor() {
		    if (utils_1.isBrowser && !Object.getOwnPropertyDescriptor(HTMLElement.prototype, 'onclick')
		        && typeof Element !== 'undefined') {
		        // WebKit https://bugs.webkit.org/show_bug.cgi?id=134364
		        // IDL interface attributes are not configurable
		        var desc = Object.getOwnPropertyDescriptor(Element.prototype, 'onclick');
		        if (desc && !desc.configurable)
		            return false;
		    }
		    Object.defineProperty(XMLHttpRequest.prototype, 'onreadystatechange', {
		        get: function () {
		            return true;
		        }
		    });
		    var req = new XMLHttpRequest();
		    var result = !!req.onreadystatechange;
		    Object.defineProperty(XMLHttpRequest.prototype, 'onreadystatechange', {});
		    return result;
		}
		;
		var unboundKey = utils_1.zoneSymbol('unbound');
		// Whenever any eventListener fires, we check the eventListener target and all parents
		// for `onwhatever` properties and replace them with zone-bound functions
		// - Chrome (for now)
		function patchViaCapturingAllTheEvents() {
		    for (var i = 0; i < eventNames.length; i++) {
		        var property = eventNames[i];
		        var onproperty = 'on' + property;
		        document.addEventListener(property, function (event) {
		            var elt = event.target, bound;
		            var source = elt.constructor['name'] + '.' + onproperty;
		            while (elt) {
		                if (elt[onproperty] && !elt[onproperty][unboundKey]) {
		                    bound = Zone.current.wrap(elt[onproperty], source);
		                    bound[unboundKey] = elt[onproperty];
		                    elt[onproperty] = bound;
		                }
		                elt = elt.parentElement;
		            }
		        }, true);
		    }
		    ;
		}
		;
	
	
	/***/ },
	/* 7 */
	/***/ function(module, exports, __webpack_require__) {
	
		/* WEBPACK VAR INJECTION */(function(global) {"use strict";
		var utils_1 = __webpack_require__(3);
		// we have to patch the instance since the proto is non-configurable
		function apply(_global) {
		    var WS = _global.WebSocket;
		    // On Safari window.EventTarget doesn't exist so need to patch WS add/removeEventListener
		    // On older Chrome, no need since EventTarget was already patched
		    if (!_global.EventTarget) {
		        utils_1.patchEventTargetMethods(WS.prototype);
		    }
		    _global.WebSocket = function (a, b) {
		        var socket = arguments.length > 1 ? new WS(a, b) : new WS(a);
		        var proxySocket;
		        // Safari 7.0 has non-configurable own 'onmessage' and friends properties on the socket instance
		        var onmessageDesc = Object.getOwnPropertyDescriptor(socket, 'onmessage');
		        if (onmessageDesc && onmessageDesc.configurable === false) {
		            proxySocket = Object.create(socket);
		            ['addEventListener', 'removeEventListener', 'send', 'close'].forEach(function (propName) {
		                proxySocket[propName] = function () {
		                    return socket[propName].apply(socket, arguments);
		                };
		            });
		        }
		        else {
		            // we can patch the real socket
		            proxySocket = socket;
		        }
		        utils_1.patchOnProperties(proxySocket, ['close', 'error', 'message', 'open']);
		        return proxySocket;
		    };
		    global.WebSocket.prototype = Object.create(WS.prototype, { constructor: { value: WebSocket } });
		}
		exports.apply = apply;
	
		/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))
	
	/***/ }
	/******/ ]);

/***/ },

/***/ 305:
/***/ function(module, exports, __webpack_require__) {

	"use strict";
	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(24);
	var device_service_1 = __webpack_require__(244);
	var searchFilterPipe_1 = __webpack_require__(306);
	var DevicesComponent = (function () {
	    function DevicesComponent(_devicesService) {
	        var _this = this;
	        this._devicesService = _devicesService;
	        this.devices = [];
	        this._devicesService.devicesSubject
	            .subscribe(function (devices) {
	            _this.devices = devices;
	        });
	        this._devicesService.load();
	        this.interval = setInterval(function () { return _this._devicesService.load(); }, 5000);
	    }
	    DevicesComponent.prototype.addRight = function (id) {
	        this._devicesService.enterRightEditModeForDevice(id).subscribe(function (res) {
	            console.log("Einrichtung erfolgt");
	        }, function (error) {
	            console.log("fehler bei der Einrichtung aufgetretten");
	        });
	    };
	    DevicesComponent.prototype.addLeft = function (id) {
	        this._devicesService.enterLeftEditModeForDevice(id).subscribe(function (res) {
	            console.log("Einrichtung erfolgt");
	        }, function (error) {
	            console.log("fehler bei der Einrichtung aufgetretten");
	        });
	    };
	    DevicesComponent.prototype.delete = function (id) {
	        this._devicesService.deleteDevice(id).subscribe(function (res) {
	            console.log("gelöscht");
	        }, function (error) {
	            console.log("fehler beim löschen aufgetretten");
	        });
	    };
	    DevicesComponent = __decorate([
	        core_1.Component({
	            selector: 'Devices',
	            pipes: [
	                searchFilterPipe_1.SearchFilterPipe
	            ],
	            template: "\n        <div class=\"input-field col s6\">\n          <input #listFilter (keyup)=\"0\" placeholder=\"Suche\" id=\"first_name\" type=\"text\" class=\"validate\">\n        </div>\n        <ul class=\"collection\">\n            <li *ngFor=\"#device of devices | searchFilter:listFilter.value\" class=\"collection-item avatar\">\n\n                <i class=\"material-icons circle\"><i class=\"material-icons\">local_florist</i></i>\n                <span class=\"title\">\n                    {{device.id}}\n                </span>\n\n                <p>\n                    {{device.state}}<br>\n                    <!--Second Line-->\n                </p>\n\n                <div class=\"secondary-content\">\n                <!--<div *ngIf=\"device.locationX >= 1000.0\">-->\n                <!--<a class=\"btn-floating btn waves-effect waves-light\"-->\n                    <!--(click)=\"addLeft(device.id)\"  >-->\n                        <!--<i class=\"material-icons\">add</i>-->\n                <!--</a>-->\n                <!--<a class=\"btn-floating btn waves-effect waves-light\"-->\n                    <!--(click)=\"addRight(device.id)\"  >-->\n                        <!--<i class=\"material-icons\">add</i>-->\n                <!--</a>-->\n                <!--</div>-->\n                <!--<div *ngIf=\"device.locationX <= 1000.0\">-->\n                    <a href=\"#/device/{{device.id}}\" class=\"btn-floating btn waves-effect waves-light\">\n                        <i class=\"material-icons\">mode_edit</i>\n                    </a>\n                    <a class=\"btn-floating btn waves-effect waves-light\" (click)=\"delete(device.id)\"  >\n                        <i class=\"material-icons\">delete</i>\n                    </a>\n                <!--</div>-->\n\n                </div>\n            </li>\n        </ul>\n\n    "
	        }), 
	        __metadata('design:paramtypes', [device_service_1.DevicesService])
	    ], DevicesComponent);
	    return DevicesComponent;
	}());
	exports.DevicesComponent = DevicesComponent;


/***/ },

/***/ 306:
/***/ function(module, exports, __webpack_require__) {

	"use strict";
	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(24);
	var SearchFilterPipe = (function () {
	    function SearchFilterPipe() {
	    }
	    SearchFilterPipe.prototype.transform = function (all, args) {
	        var toFilter = args[0].toLocaleLowerCase();
	        return all.filter(function (device) { return device.id.toLocaleLowerCase().indexOf(toFilter) !== -1; });
	    };
	    SearchFilterPipe = __decorate([
	        core_1.Pipe({ name: 'searchFilter' }), 
	        __metadata('design:paramtypes', [])
	    ], SearchFilterPipe);
	    return SearchFilterPipe;
	}());
	exports.SearchFilterPipe = SearchFilterPipe;


/***/ },

/***/ 307:
/***/ function(module, exports, __webpack_require__) {

	"use strict";
	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(24);
	var DocsComponent = (function () {
	    function DocsComponent() {
	    }
	    DocsComponent = __decorate([
	        core_1.Component({
	            selector: 'Docs',
	            template: "\n\n        <div class=\"slider fullscreen\">\n            <ul class=\"slides\">\n                <li>\n                    <img onload=\"myFunction()\" src=\"assets/img/handbuch_1.png\"> <!-- random image -->\n                    <div class=\"caption center-align\">\n                        <h5 class=\"light grey-text text-lighten-3\">\n                            Stellen Sie sich in den Erkennungsbereich Ihrer Kinect mit einem Abstand von 1-2 Metern.\n                        </h5>\n                    </div>\n                </li>\n                <li>\n                    <img src=\"assets/img/handbuch_2.png\"> <!-- random image -->\n                    <div class=\"caption center-align\">\n\n                        <h5 class=\"light grey-text text-lighten-3\">\n                            Zeigen Sie mit einem Arm auf das Ger\u00E4t, das eingerichtet werden soll.\n                        </h5>\n                    </div>\n                </li>\n                <li>\n                    <img src=\"assets/img/handbuch_3.png\"> <!-- random image -->\n                    <div class=\"caption center-align\">\n                        <h5 class=\"light grey-text text-lighten-3\">\n                            Heben Sie den anderen Arm hoch, sodass dieser oberhalb Ihrer Schulter ist.\n                        </h5>\n                    </div>\n                </li>\n                <li>\n                    <img src=\"assets/img/handbuch_4.png\"> <!-- random image -->\n                    <div class=\"caption center-align\">\n                        <h5 class=\"light grey-text text-lighten-3\">\n                            Bewegen Sie sich nun einen Meter nach rechts oder links.\n                            Die Position des Armes \u00FCber der Schulter wird nicht ver\u00E4ndert.\n                            ...\n                        </h5>\n                    </div>\n                </li>\n                <li>\n                    <img src=\"assets/img/handbuch_4.png\"> <!-- random image -->\n                    <div class=\"caption center-align\">\n                        <h5 class=\"light grey-text text-lighten-3\">\n                            ... Der Arm, der auf das Ger\u00E4t zeigt, sollte nach wie vor auf das Ger\u00E4t zeigen\n                        </h5>\n                    </div>\n                </li>\n                <li>\n                    <img src=\"assets/img/handbuch_5.png\"> <!-- random image -->\n                    <div class=\"caption center-align\">\n                        <h5 class=\"light grey-text text-lighten-3\">\n                            Nun wird der Arm, der sich \u00FCber Schulterh\u00F6he befindet, gesenkt.\n                            Der andere Arm zeigt weiterhin auf das Ger\u00E4t.\n                        </h5>\n                    </div>\n                </li>\n                <li>\n                    <img src=\"assets/img/handbuch_6.png\"> <!-- random image -->\n                    <div class=\"caption center-align\">\n                        <h5 class=\"light grey-text text-lighten-3\">\n                        Nun k\u00F6nnen Sie den anderen Arm wird \u00FCber Schulterh\u00F6he heben.\n                        Sollte Ihr gew\u00FCnschtes Ger\u00E4t nun eingeschaltet sein,\n                        ...\n                        </h5>\n                    </div>\n                </li>\n                <li>\n                    <img src=\"assets/img/handbuch_6.png\"> <!-- random image -->\n                    <div class=\"caption center-align\">\n                        <h5 class=\"light grey-text text-lighten-3\">\n                        ... haben Sie alles richtig gemacht. Sollte dies nicht der Fall sein,\n                        so sollten Sie die Konfiguration noch einmal wiederholen.\n                        </h5>\n                    </div>\n                </li>\n            </ul>\n        </div>\n\n\n        "
	        }), 
	        __metadata('design:paramtypes', [])
	    ], DocsComponent);
	    return DocsComponent;
	}());
	exports.DocsComponent = DocsComponent;


/***/ },

/***/ 308:
/***/ function(module, exports, __webpack_require__) {

	"use strict";
	var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
	    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
	    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
	    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
	    return c > 3 && r && Object.defineProperty(target, key, r), r;
	};
	var __metadata = (this && this.__metadata) || function (k, v) {
	    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
	};
	var core_1 = __webpack_require__(24);
	var device_service_1 = __webpack_require__(244);
	var router_1 = __webpack_require__(246);
	var comon_1 = __webpack_require__(245);
	var device_service_2 = __webpack_require__(244);
	var device_service_3 = __webpack_require__(244);
	var DeviceDetailComponent = (function () {
	    function DeviceDetailComponent(_routeParams, _devicesService) {
	        var _this = this;
	        this._routeParams = _routeParams;
	        this._devicesService = _devicesService;
	        this.device = new device_service_1.Device;
	        this.possibleSets = [];
	        this.activSetTrunOffGesture = new device_service_2.ActivSet("", "");
	        this.activSetTrunOnGesture = new device_service_2.ActivSet("", "");
	        this.id = this._routeParams.get('id');
	        this._devicesService.devicesSubject
	            .subscribe(function (devices) {
	            var index = comon_1.indexOfId(devices, _this.id);
	            _this.device = devices[index];
	            if (_this.device.possibleSets !== null) {
	                _this.possibleSets = _this.device.possibleSets;
	                _this.activSetTrunOffGesture = _this.device.activSets.activSetTrunOffGesture;
	                _this.activSetTrunOnGesture = _this.device.activSets.activSetTrunOnGesture;
	            }
	        });
	        this._devicesService.load();
	    }
	    DeviceDetailComponent.prototype.addRight = function (id) {
	        this._devicesService.enterRightEditModeForDevice(id).subscribe(function (res) {
	            console.log("Einrichtung erfolgt");
	        }, function (error) {
	            console.log("fehler bei der Einrichtung aufgetretten");
	        });
	    };
	    DeviceDetailComponent.prototype.addLeft = function (id) {
	        this._devicesService.enterLeftEditModeForDevice(id).subscribe(function (res) {
	            console.log("Einrichtung erfolgt");
	        }, function (error) {
	            console.log("fehler bei der Einrichtung aufgetretten");
	        });
	    };
	    DeviceDetailComponent.prototype.delete = function (id) {
	        this._devicesService.deleteDevice(id).subscribe(function (res) {
	            console.log("gelöscht");
	        }, function (error) {
	            console.log("fehler beim löschen aufgetretten");
	        });
	    };
	    DeviceDetailComponent.prototype.activateSetTrunOffGesture = function (possibleSet, arg) {
	        this.activSetTrunOffGesture = new device_service_2.ActivSet(possibleSet.name, arg);
	    };
	    DeviceDetailComponent.prototype.activateSetTrunONGesture = function (possibleSet, arg) {
	        this.activSetTrunOnGesture = new device_service_2.ActivSet(possibleSet.name, arg);
	    };
	    DeviceDetailComponent.prototype.save = function () {
	        this._devicesService.save(this.id, new device_service_3.ActivSets(this.activSetTrunOffGesture, this.activSetTrunOnGesture));
	    };
	    DeviceDetailComponent = __decorate([
	        core_1.Component({
	            selector: 'DeviceDetail',
	            template: "\n        <h3>{{id}}</h3>\n        <div class=\"row\">\n            <div class=\"col s12 m6\">\n                <div class=\"card\">\n                    <div class=\"card-content\">\n                        <span class=\"card-title\">Linke Hand konfigurieren</span>\n                        <p>I am a very simple card. I am good at containing small bits of information.\n                            I am convenient because I require little markup to use effectively.</p>\n                    </div>\n                    <div class=\"card-action\">\n                        <a class=\"waves-effect waves-light btn\" (click)=\"addLeft(id)\">\n                            <i class=\"material-icons\">add</i>\n                        </a>\n                    </div>\n                </div>\n            </div>\n            <div class=\"col s12 m6\">\n                <div class=\"card\">\n                    <div class=\"card-content\">\n                        <span class=\"card-title\">Rechte Hand konfigurieren</span>\n                        <p>I am a very simple card. I am good at containing small bits of information.\n                            I am convenient because I require little markup to use effectively.</p>\n                    </div>\n                    <div class=\"card-action\">\n                        <a class=\"waves-effect waves-light btn\" (click)=\"addRight(id)\">\n                            <i class=\"material-icons\">add</i>\n                        </a>\n                    </div>\n                </div>\n            </div>\n            <div class=\"col s12 m12\">\n                <div class=\"card-content\">\n                    <div class=\"card row\">\n\n                        <div class=\"col s12 m6\">\n\n                            <span class=\"card-title\">TurnOff Geste</span>\n                            <ul class=\"collection\">\n                                <li *ngFor=\"#possibleSet of possibleSets\" class=\"collection-item\"\n\n                                    [ngClass]=\"{active: activSetTrunOffGesture.arg === null &&\n                                                    activSetTrunOffGesture.name === possibleSet.name }\">\n\n                                                    <span (click)=\"activateSetTrunOffGesture(possibleSet, null)\">\n                                                        {{possibleSet.name}}\n                                                    </span>\n\n                                    <ul class=\"\">\n                                        <li *ngFor=\"#arg of possibleSet.args\" class=\"collection-item\"\n                                            (click)=\"activateSetTrunOffGesture(possibleSet, arg)\"\n\n                                            [ngClass]=\"{active: activSetTrunOffGesture.arg === arg &&\n                                                            activSetTrunOffGesture.name === possibleSet.name }\">\n\n                                            {{arg}}\n                                        </li>\n                                    </ul>\n\n                                </li>\n                            </ul>\n                        </div>\n\n\n                        <div class=\"col s12 m6\">\n\n                            <span class=\"card-title\">TurnOn Geste</span>\n                            <ul class=\"collection\">\n                                <li *ngFor=\"#possibleSet of possibleSets\" class=\"collection-item\"\n\n                                    [ngClass]=\"{active: activSetTrunOnGesture.arg === null &&\n                                                        activSetTrunOnGesture.name === possibleSet.name }\">\n\n                                                        <span (click)=\"activateSetTrunONGesture(possibleSet, null)\">\n                                                            {{possibleSet.name}}\n                                                        </span>\n\n                                    <ul class=\"\">\n                                        <li *ngFor=\"#arg of possibleSet.args\" class=\"collection-item\"\n                                            (click)=\"activateSetTrunONGesture(possibleSet, arg)\"\n                                            [ngClass]=\"{active: activSetTrunOnGesture.arg === arg &&\n                                                            activSetTrunOnGesture.name === possibleSet.name }\">\n                                            {{arg}}\n                                        </li>\n                                    </ul>\n                                </li>\n                            </ul>\n\n                            <div class=\"card-action\">\n                                <a class=\"waves-effect waves-light btn\"\n                                   (click)=\"save()\">\n                                    speichern\n                                </a>\n                            </div>\n                        </div>\n\n                    </div>\n                </div>\n\n            </div>\n        </div>\n    "
	        }), 
	        __metadata('design:paramtypes', [router_1.RouteParams, device_service_1.DevicesService])
	    ], DeviceDetailComponent);
	    return DeviceDetailComponent;
	}());
	exports.DeviceDetailComponent = DeviceDetailComponent;


/***/ }

});
//# sourceMappingURL=app.js.map