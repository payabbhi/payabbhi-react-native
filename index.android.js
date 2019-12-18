
'use strict';

import { NativeModules, NativeEventEmitter } from 'react-native';

const payabbhiEvents = new NativeEventEmitter(NativeModules.PayabbhiEventEmitter);

const removeSubscriptions = () => {
  payabbhiEvents.removeAllListeners('Payabbhi::PAYMENT_SUCCESS');
  payabbhiEvents.removeAllListeners('Payabbhi::PAYMENT_ERROR');
};

class PayabbhiCheckout {
  static open(options, successCallback, errorCallback) {
    return new Promise(function(resolve, reject) {
      payabbhiEvents.addListener('Payabbhi::PAYMENT_SUCCESS', (data) => {
        let resolveFn = successCallback || resolve;
        resolveFn(data);
        removeSubscriptions();
      });
      payabbhiEvents.addListener('Payabbhi::PAYMENT_ERROR', (data) => {
        let rejectFn = errorCallback || reject;
        rejectFn(data);
        removeSubscriptions();
      });
      NativeModules.PayabbhiCheckout.open(options);
    });
  }
}

export default PayabbhiCheckout;
