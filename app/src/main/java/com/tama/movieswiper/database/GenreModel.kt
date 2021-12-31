/**
* Simple model that will be saved in the Firebase database. Based on this model, the recommendations are selected
*/

package com.tama.movieswiper.database

data class GenreModel(
    var action: Int = 50,
    var adventure: Int = 50,
    var animation: Int = 50,
    var comedy: Int = 50,
    var crime: Int = 50,
    var documentary: Int = 50,
    var drama: Int = 50,
    var family: Int = 50,
    var fantasy: Int = 50,
    var history: Int = 50,
    var horror: Int = 50,
    var music: Int = 50,
    var mystery: Int = 50,
    var romance: Int = 50,
    var sci_fi: Int = 50,
    var tv_movie: Int = 50,
    var thriller: Int = 50,
    var war: Int = 50,
    var western: Int = 50
)
