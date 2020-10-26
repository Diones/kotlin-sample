package org.diones.kotlinsample.card.repositories

import org.diones.kotlinsample.card.domains.CardDomain
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CardRepository : MongoRepository<CardDomain, String>