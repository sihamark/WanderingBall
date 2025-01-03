import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    
    init() {
        SamayouwaIosApp.shared.initialize()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
