import 'dart:async';

import 'package:flutter/services.dart';

class SmsReceiver {
  static const MethodChannel _channel =
      const MethodChannel('com.oval.sms_receiver');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
