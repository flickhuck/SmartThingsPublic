/**
 *  HVAC Leak Auto Off
 *
 *  Author: Fred Schank
 *  Date: 2015-09-23
 */

definition(
    name: "A/C Leak Thermostat Auto Off",
    namespace: "flickhuck",
    author: "Fred Schank",
    description: "Automatically turn off thermostat when leak detected.",
    category: "Green Living",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Developers/dry-the-wet-spot.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Developers/dry-the-wet-spot@2x.png"
)

preferences {
	section("Control") {
		input("thermostat", "capability.thermostat", title: "Thermostat")
	}
    section("When water is sensed...") {
		input("sensor", "capability.waterSensor", title: "Where?", required: true, multiple: true)
	}
}

def installed() {
 	subscribe(sensor, "water", waterHandler)
}

def updated() {
	unsubscribe()
 	subscribe(sensor, "water", waterHandler)
}

def waterHandler(evt) {
	log.debug "Sensor says ${evt.value}"
	if (evt.value == "wet") {
		turnOff()
	}
}

def turnOff() {
	log.debug "Turning off thermostat due to contact open"
	state.thermostatMode = thermostat.currentValue("thermostatMode")
	thermostat.off()
    state.changed = true
    log.debug "State: $state"
}