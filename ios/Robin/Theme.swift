import SwiftUI

/// Placeholder tokens from docs/design/design-brief.md; replace once the design is final.
enum RobinColors {
    static let paper = Color(hex: 0xFAF6EE)
    static let ink = Color(hex: 0x1F2A44)
    static let robin = Color(hex: 0xE4572E)
    static let racingGreen = Color(hex: 0x1E4D3A)
    static let amber = Color(hex: 0xF2A541)
    static let fog = Color(hex: 0xC9CED6)

    private static let lines: [String: Color] = [
        "line-values": Color(hex: 0x6C4AB6),
        "line-uk": Color(hex: 0x2E86AB),
        "line-history": Color(hex: 0xB5651D),
        "line-society": Color(hex: 0x2A9D8F),
        "line-government": Color(hex: 0x264653),
    ]

    static func line(_ token: String) -> Color { lines[token] ?? ink }
}

extension Color {
    init(hex: UInt32) {
        self.init(
            red: Double((hex >> 16) & 0xFF) / 255,
            green: Double((hex >> 8) & 0xFF) / 255,
            blue: Double(hex & 0xFF) / 255
        )
    }
}
