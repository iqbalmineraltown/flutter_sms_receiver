import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
// import 'package:sms_receiver/sms_receiver.dart';

void main() {
  const MethodChannel channel = MethodChannel('sms_receiver');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    // expect(await SmsReceiver().platformVersion, '42');
  });
}
