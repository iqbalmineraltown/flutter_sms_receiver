import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:sms_receiver/sms_receiver.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Waiting for messages...';
  SmsReceiver _smsReceiver;

  @override
  void initState() {
    super.initState();
    _smsReceiver = SmsReceiver(onSmsReceived, onTimeout: onTimeout);
    _startListening();
    //initPlatformState();
  }

  void onSmsReceived(String pin) {
    setState(() {
      _platformVersion = "The pin is: $pin";
    });
  }

  void onTimeout() {
    setState(() {
      _platformVersion = "Timeout!!!";
    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await _smsReceiver.platformVersion();
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  void _startListening() {
    _smsReceiver.startListening();
    setState(() {
      _platformVersion = "Waiting for messages...";
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('SMS listener app'),
        ),
        body: Center(
          child: Text(_platformVersion),
        ),
      ),
    );
  }
}
