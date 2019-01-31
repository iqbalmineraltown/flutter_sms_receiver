# sms_receiver

Flutter plugin for reading incoming-and-expected SMS only

Currently developed for Android: reading message without requesting SMS permission.

## Usage

- Generate acceptable [message format](https://developers.google.com/identity/sms-retriever/verify)

  - DON'T generate hash message on runtime using `AppSignatureHelper`. Store hash as constant inside your app before building app package.

- Add dependency to this package

- Create `SmsReceiver()` with `onSmsReceived(String message)` handler.

- Start listening with `startListening()`

- Once sms received or receiver timed out, use `startListening` again. See example
  - Receiver timeout is 5 minutes. Currently not configurable.

## Future Development

- Add/Combine with iOS counterpart
