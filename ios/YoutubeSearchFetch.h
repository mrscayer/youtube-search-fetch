
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNYoutubeSearchFetchSpec.h"

@interface YoutubeSearchFetch : NSObject <NativeYoutubeSearchFetchSpec>
#else
#import <React/RCTBridgeModule.h>

@interface YoutubeSearchFetch : NSObject <RCTBridgeModule>
#endif

@end
