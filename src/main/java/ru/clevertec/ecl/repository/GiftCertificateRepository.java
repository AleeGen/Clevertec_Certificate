package ru.clevertec.ecl.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.ecl.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

    /**
     * Return list of gift certificates that meet the condition.
     *
     * @param tagName  tag name
     * @param part     part of the gift certificate name or description
     * @param pageable pagination
     * @return - list of gift certificates that meet the condition
     */
    @Query("""
            from GiftCertificate gc join gc.tags t
            where (:tagName = null or t.name = :tagName)
            and (:part = null or gc.name like %:part% or gc.description like %:part%)
            """)
    List<GiftCertificate> findAll(@Param("tagName") String tagName, @Param("part") String part, Pageable pageable);

}