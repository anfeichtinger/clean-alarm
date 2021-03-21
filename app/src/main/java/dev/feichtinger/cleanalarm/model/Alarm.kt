package dev.feichtinger.cleanalarm.model

import com.google.gson.Gson
import java.io.Serializable
import java.util.*

class Alarm(val label: String, var active: Boolean, val time: Calendar) : Serializable {

    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }

    override fun toString(): String {
        return "Alarm(label='$label', active=$active, time=${time.time}, hashCode=${hashCode()})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Alarm

        if (label != other.label) return false
        if (active != other.active) return false
        if (time != other.time) return false

        return true
    }

    override fun hashCode(): Int {
        var result = label.hashCode()
        result = 31 * result + active.hashCode()
        result = 31 * result + time.hashCode()
        return result
    }


    companion object {
        fun fromJson(json: String): Alarm {
            val gson = Gson()
            return gson.fromJson(json, Alarm::class.java)
        }
    }
}