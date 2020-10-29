package com.jakode.covid19.data.database.mapper

import com.jakode.covid19.data.database.model.GlobalCacheEntity
import com.jakode.covid19.data.database.model.StatisticsCacheEntity
import com.jakode.covid19.model.Global
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.utils.EntityMapper
import javax.inject.Inject

class CacheMapper @Inject constructor() : EntityMapper<StatisticsCacheEntity, Statistics> {
    override fun mapFromEntity(entity: StatisticsCacheEntity): Statistics {
        return Statistics(
            continent = entity.continent,
            country = entity.country,
            population = entity.population,
            cases = Statistics.Cases(
                new = entity.cases.newCases,
                active = entity.cases.activeCases,
                critical = entity.cases.criticalCases,
                recovered = entity.cases.recoveredCases,
                total = entity.cases.totalCases
            ),
            deaths = Statistics.Deaths(
                new = entity.deaths.newDeaths,
                total = entity.deaths.totalDeaths
            ),
            tests = Statistics.Tests(entity.tests.totalTests),
            day = entity.day,
            time = entity.time
        )
    }

    override fun mapToEntity(domainModel: Statistics): StatisticsCacheEntity {
        return StatisticsCacheEntity(
            continent = domainModel.continent,
            country = domainModel.country,
            population = domainModel.population,
            cases = StatisticsCacheEntity.Cases(
                newCases = domainModel.cases.new,
                activeCases = domainModel.cases.active,
                criticalCases = domainModel.cases.critical,
                recoveredCases = domainModel.cases.recovered,
                totalCases = domainModel.cases.total
            ),
            deaths = StatisticsCacheEntity.Deaths(
                newDeaths = domainModel.deaths.new,
                totalDeaths = domainModel.deaths.total
            ),
            tests = StatisticsCacheEntity.Tests(domainModel.tests.total),
            day = domainModel.day,
            time = domainModel.time
        )
    }

    fun mapFromEntityList(entities: List<StatisticsCacheEntity>): List<Statistics> {
        return entities.map { mapFromEntity(it) }
    }

    fun mapFromGlobal(entity: GlobalCacheEntity): Global {
        return Global(
            confirmed = entity.confirmed,
            deaths = entity.deaths,
            recovered = entity.recovered,
            active = entity.active,
            fatalityRate = entity.fatalityRate,
            lastUpdate = entity.lastUpdate
        )
    }

    fun mapToGlobal(domainModel: Global): GlobalCacheEntity {
        return GlobalCacheEntity(
            confirmed = domainModel.confirmed,
            deaths = domainModel.deaths,
            recovered = domainModel.recovered,
            active = domainModel.active,
            fatalityRate = domainModel.fatalityRate,
            lastUpdate = domainModel.lastUpdate,
            location = "Global"
        )
    }
}