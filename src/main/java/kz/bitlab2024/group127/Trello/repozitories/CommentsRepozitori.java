package kz.bitlab2024.group127.Trello.repozitories;

import jakarta.transaction.Transactional;
import kz.bitlab2024.group127.Trello.entites.Comments;
import kz.bitlab2024.group127.Trello.entites.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional

public interface CommentsRepozitori extends JpaRepository<Comments, Long> {
}
