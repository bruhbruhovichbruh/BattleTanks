package r.v.stoyanov.battletanks.enums

enum class Material(val tankCanGoThrough: Boolean) {
    EMPTY(true),
    BRICK(false),
    CONCRETE(false),
    GRASS(true)
}
