package io.nightrider.dailyreads.author;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

public interface AuthorRepository extends CassandraRepository<Author, String>{
    
}
