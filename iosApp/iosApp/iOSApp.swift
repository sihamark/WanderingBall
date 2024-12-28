import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        IosMain.shared.initalize()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
