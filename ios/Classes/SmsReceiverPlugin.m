#import "SmsReceiverPlugin.h"
#import <sms_receiver/sms_receiver-Swift.h>

@implementation SmsReceiverPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftSmsReceiverPlugin registerWithRegistrar:registrar];
}
@end
