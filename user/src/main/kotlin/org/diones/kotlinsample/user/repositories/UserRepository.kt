package org.diones.kotlinsample.user.repositories

import org.diones.kotlinsample.user.domains.UserDomain
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<UserDomain, String>