import 'package:flutter/material.dart';
import 'package:sms_receiver/sms_receiver.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _textContent = 'Waiting for messages...';
  SmsReceiver _smsReceiver;

  @override
  void initState() {
    super.initState();
    _smsReceiver = SmsReceiver(onSmsReceived, onTimeout: onTimeout);
    _startListening();
  }

  void onSmsReceived(String message) {
    setState(() {
      _textContent = message;
    });
  }

  void onTimeout() {
    setState(() {
      _textContent = "Timeout!!!";
    });
  }

  void _startListening() {
    _smsReceiver.startListening();
    setState(() {
      _textContent = "Waiting for messages...";
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('SMS Listener App'),
        ),
        body: Column(
          children: <Widget>[
            Container(
              padding: const EdgeInsets.symmetric(vertical: 16.0),
              alignment: Alignment.center,
              child: Text(_textContent),
            ),
            RaisedButton(
              child: Text("Listen Again"),
              onPressed: _startListening,
            ),
          ],
        ),
      ),
    );
  }
}
