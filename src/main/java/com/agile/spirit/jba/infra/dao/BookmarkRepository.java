package com.agile.spirit.jba.infra.dao;

import org.springframework.data.repository.CrudRepository;

import com.agile.spirit.jba.domain.Bookmark;

public interface BookmarkRepository extends CrudRepository<Bookmark, Long>, BookmarkRepositoryCustom {

}
