# sms_receiver

NOTE: This plugin might no longer needed due to [flutter/flutter#13015](https://github.com/flutter/flutter/issues/13015#issuecomment-624921951)

[![pub package](https://img.shields.io/badge/pub-0.4.1-blue.svg)](https://pub.dev/packages/sms_receiver)

Flutter plugin for reading incoming-and-expected SMS only

Currently developed for Android: reading message without requesting SMS permission.

## Usage

- Generate acceptable [message format](https://developers.google.com/identity/sms-retriever/verify)

  - DON'T generate hash message on runtime using `AppSignatureHelper`. Store hash as constant inside your app, either on the server or client.

- Add dependency to this package

- Create `SmsReceiver()` with `onSmsReceived(String message)` handler.

- Start listening with `startListening()`

- Stop listening with `stopListening()`

- Once sms received or receiver timed out, use `startListening` again. See example
  - Receiver timeout is 5 minutes. Currently not configurable.

## Future Development

- Add/Combine with iOS counterpart

