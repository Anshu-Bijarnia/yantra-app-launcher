package com.coderGtm.yantra.commands.weather

import android.content.pm.PackageManager
import android.graphics.Typeface
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.coderGtm.yantra.blueprints.BaseCommand
import com.coderGtm.yantra.models.CommandMetadata
import com.coderGtm.yantra.terminal.Terminal

class Command(terminal: Terminal) : BaseCommand(terminal) {
    override val metadata = CommandMetadata(
        name = "weather",
        helpTitle = "weather [location]",
        description = "Fetches the weather report for specified location. Example w new delhi"
    )

    override fun execute(command: String) {
        val args = command.split(" ")
        if (args.size < 2) {
            output("Please specify a location", terminal.theme.errorTextColor)
            return
        }
        val location = command.trim().removePrefix(args[0]).trim()
        output("Fetching weather report of $location...", terminal.theme.resultTextColor, Typeface.ITALIC)
        val apiKey = terminal.activity.packageManager.getApplicationInfo(terminal.activity.packageName, PackageManager.GET_META_DATA).metaData["OPEN_WEATHER_API_KEY"]
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$location&appid=$apiKey"
        val queue = Volley.newRequestQueue(terminal.activity)
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                handleResponse(response, this@Command, location)
            },
            { error ->
                handleError(error, this@Command)
            })
        queue.add(stringRequest)
    }
}