import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    
    init() {
        SamayouwaIosApp.shared.initalize()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
