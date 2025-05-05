package com.kaankarakas.librarymanagement.mapper.borrowhistory;

import com.kaankarakas.librarymanagement.domain.borrowhistory.BorrowHistory;
import com.kaankarakas.librarymanagement.dto.response.borrowhistory.BorrowHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BorrowHistoryMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "book.title", target = "bookTitle")
    BorrowHistoryDTO toDTO(BorrowHistory entity);
}
