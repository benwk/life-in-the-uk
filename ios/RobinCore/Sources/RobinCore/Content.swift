import Foundation

/// Text keyed by locale code ("en", "zh-Hans", …). English is always present.
public typealias LocalizedText = [String: String]

public extension Dictionary where Key == String, Value == String {
    /// The text in `locale`, falling back to English.
    func localized(_ locale: String) -> String {
        self[locale] ?? self["en"] ?? ""
    }

    /// The text in `locale` only if it has been translated.
    func translation(_ locale: String) -> String? {
        locale == "en" ? nil : self[locale]
    }

    var english: String { self["en"] ?? "" }
}

/// The single bundle built by tools/build_content.py and shipped in the app.
public struct ContentBundle: Codable, Sendable {
    public let meta: Meta
    public let lines: [Line]
    public let facts: [Fact]
    public let questions: [Question]
    public let terms: [Term]

    public var stations: [Station] { lines.flatMap(\.stations) }

    public func line(containing stationId: String) -> Line? {
        lines.first { $0.stations.contains { $0.id == stationId } }
    }

    public func facts(for stationId: String) -> [Fact] {
        facts.filter { $0.stationId == stationId }
    }

    public func questions(for stationId: String) -> [Question] {
        questions.filter { $0.stationId == stationId }
    }

    public static func decode(_ data: Data) throws -> ContentBundle {
        try JSONDecoder().decode(ContentBundle.self, from: data)
    }
}

public struct Meta: Codable, Sendable {
    public let contentVersion: String
    public let handbook: Handbook
    public let locales: [ContentLocale]
    public let exam: ExamRules
    public let miniMock: MockRules
    public let dailyTeaBreak: TeaBreakRules
}

public struct Handbook: Codable, Sendable {
    public let title: String
    public let edition: String
}

public struct ContentLocale: Codable, Sendable, Identifiable {
    public let code: String
    public let name: String
    public let dir: String

    public var id: String { code }
    public var isRightToLeft: Bool { dir == "rtl" }
}

public struct ExamRules: Codable, Sendable {
    public let questionCount: Int
    public let durationMinutes: Int
    public let passMark: Int

    public init(questionCount: Int, durationMinutes: Int, passMark: Int) {
        self.questionCount = questionCount
        self.durationMinutes = durationMinutes
        self.passMark = passMark
    }
}

public struct MockRules: Codable, Sendable {
    public let questionCount: Int
    public let durationMinutes: Int
}

public struct TeaBreakRules: Codable, Sendable {
    public let questionCount: Int
}

public struct Line: Codable, Sendable, Identifiable {
    public let id: String
    public let chapter: Int
    public let color: String
    public let title: LocalizedText
    public let stations: [Station]
}

public struct Station: Codable, Sendable, Identifiable, Hashable {
    public let id: String
    public let title: LocalizedText
    public let summary: LocalizedText?
    public let minutes: Int
    public let stampIcon: String
}

public struct Fact: Codable, Sendable, Identifiable {
    public let id: String
    public let stationId: String
    public let kind: String
    public let year: Int?
    public let text: LocalizedText
    public let memoryHook: LocalizedText?
    public let examNote: LocalizedText?
    public let termIds: [String]?
    public let source: Source
}

public struct Source: Codable, Sendable {
    public let chapter: Int
    public let section: String
}

public enum QuestionType: String, Codable, Sendable {
    case single
    case multiple
    case trueFalse = "true_false"
    case statement
    case order
    case match

    /// Types that appear in the real test and therefore in mock exams.
    public var isExamType: Bool { self != .order && self != .match }
}

public struct Question: Codable, Sendable, Identifiable, Hashable {
    public let id: String
    public let stationId: String
    public let factIds: [String]
    public let type: QuestionType
    public let prompt: LocalizedText
    public let options: [Option]
    public let answer: [String]
    public let explanation: LocalizedText
    public let difficulty: Int
    public let examEligible: Bool
    public let traps: [String]?
}

public struct Option: Codable, Sendable, Identifiable, Hashable {
    public let id: String
    public let text: LocalizedText
    public let matchText: LocalizedText?
}

public struct Term: Codable, Sendable, Identifiable {
    public let id: String
    public let term: String
    public let ipa: String?
    public let definition: LocalizedText
}
