package com.jakode.covid19.data.api.mapper

import com.jakode.covid19.data.api.model.GlobalNetworkEntity
import com.jakode.covid19.data.api.model.StatisticsNetworkEntity
import com.jakode.covid19.model.Global
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.utils.EntityMapper
import javax.inject.Inject

class NetworkMapper @Inject constructor() : EntityMapper<StatisticsNetworkEntity, Statistics> {
    override fun mapFromEntity(entity: StatisticsNetworkEntity): Statistics {
        return Statistics(
            continent = entity.continent,
            country = entity.country,
            population = entity.population,
            cases = Statistics.Cases(
                new = entity.cases.new,
                active = entity.cases.active,
                critical = entity.cases.critical,
                recovered = entity.cases.recovered,
                total = entity.cases.total
            ),
            deaths = Statistics.Deaths(
                new = entity.deaths.new,
                total = entity.deaths.total
            ),
            tests = Statistics.Tests(entity.tests.total),
            day = entity.day,
            time = entity.time
        )
    }

    override fun mapToEntity(domainModel: Statistics): StatisticsNetworkEntity {
        return StatisticsNetworkEntity(
            continent = domainModel.continent,
            country = domainModel.country,
            population = domainModel.population,
            cases = StatisticsNetworkEntity.Cases(
                new = domainModel.cases.new,
                active = domainModel.cases.active,
                critical = domainModel.cases.critical,
                recovered = domainModel.cases.recovered,
                total = domainModel.cases.total
            ),
            deaths = StatisticsNetworkEntity.Deaths(
                new = domainModel.deaths.new,
                total = domainModel.deaths.total
            ),
            tests = StatisticsNetworkEntity.Tests(domainModel.tests.total),
            day = domainModel.day,
            time = domainModel.time
        )
    }

    fun mapFromEntityList(entities: List<StatisticsNetworkEntity>): List<Statistics> {
        return entities.map { mapFromEntity(it) }
    }

    fun mapFromGlobal(entity: GlobalNetworkEntity): Global {
        return Global(
            confirmed = entity.confirmed,
            deaths = entity.deaths,
            recovered = entity.recovered,
            active = entity.active,
            fatalityRate = entity.fatalityRate,
            lastUpdate = entity.lastUpdate,
        )
    }
}