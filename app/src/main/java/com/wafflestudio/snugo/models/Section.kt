package com.wafflestudio.snugo.models

enum class Section {
    A,
    B,
    C,
    D,
    E,
    F,
    G,
    H,
    I,
    J,
    K,
    ;

    override fun toString(): String {
        return when (this) {
            A -> "A"
            B -> "B"
            C -> "C"
            D -> "D"
            E -> "E"
            F -> "F"
            G -> "G"
            H -> "H"
            I -> "I"
            J -> "J"
            K -> "K"
        }
    }
}
