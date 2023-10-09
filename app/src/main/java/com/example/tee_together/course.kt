package com.example.tee_together

class course(
    private var name: String = "",
    private var pars: MutableList<Int> = mutableListOf(),
    private var comments: MutableList<String> = mutableListOf(),
    private var ratings: MutableList<Double> = mutableListOf(),
) {
    fun setName(name: String) {
        this.name = name
    }
    fun getName(): String {
        return name
    }

    fun setPars(pars: List<Int>) {
        this.pars.clear()
        this.pars.addAll(pars)
    }
    fun getPars(): List<Int> {
        return pars
    }

    fun addComment(comment: String) {
        comments.add(comment)
    }
    fun getComments(): List<String> {
        return comments
    }

    fun addRating(rating: Double) {
        ratings.add(rating)
    }
    fun getRatings(): List<Double> {
        return ratings
    }
    fun calculateAverageRating(): Double {
        if (ratings.isEmpty()){
            return 0.0
        }
        val rating = ratings.sum()
        return rating / ratings.size
    }
}
