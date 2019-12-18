# react-native-payabbhi

React Native plugin for using Payabbhi native SDKs (Android & iOS)

## Installation

```bash
$ npm i react-native-payabbhi --save
$ react-native link react-native-payabbhi
```

## Usage

1. Import PayabbhiCheckout module to your component:
    ```js
    import PayabbhiCheckout from 'react-native-payabbhi';
    ```

2. Call `PayabbhiCheckout.open` method with the argument `options`. This method will
return a `JS Promise` where `then` part corresponds to a successful payment
and the `catch` part corresponds to payment failure.

    ```js
    <TouchableHighlight onPress={() => {
      var options = {
        access_id: "<access_id>",
        order_id: "<order_id>",
        amount: <amount>,
        description: "<description>",
        prefill: {
          name: "<name>",
          email: "<email>",
          contact: "<contact>"
        },
        notes: {
          merchant_order_id: "<merchant_order_id>"
        }
      };
      PayabbhiCheckout.open(options).then((data) => {
        alert(`Success: ${data.order_id} | ${data.payment_id}`);
      }).catch((error) => {
        alert(`Error: ${error.code} | ${error.message}`);
      });
    }}>
    <Text style = {styles.text}>PAY</Text>
    </TouchableHighlight>
    ```
