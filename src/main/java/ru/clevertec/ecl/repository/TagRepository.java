package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByName(String name);

    /**
     * @return the most frequently used tag from the user with the largest amount of orders
     */
   @Query(nativeQuery = true, value = """
           select tag_id as id, name
           from tag t
             join (select tag_id
               from gift_certificate_tag gct
                 join (select gift_certificate_id
                   from orders o
                     join ((select users_id
                       from orders
                       group by users_id
                       order by sum(cost) desc
                       limit 1)) tab using (users_id)) tab2 using (gift_certificate_id)
               group by tag_id
               order by count(tag_id) desc
               limit 1) tab3 on t.id = tab3.tag_id
                      """)
    Tag findMostUsedByUser();

}