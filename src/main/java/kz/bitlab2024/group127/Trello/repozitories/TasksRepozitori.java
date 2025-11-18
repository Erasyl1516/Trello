package kz.bitlab2024.group127.Trello.repozitories;

import jakarta.transaction.Transactional;
import kz.bitlab2024.group127.Trello.entites.Folders;
import kz.bitlab2024.group127.Trello.entites.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface TasksRepozitori extends JpaRepository<Tasks, Long> {
    List<Tasks> findAllByFolder(Folders folders);
    void deleteAllByFolder(Folders folders);
}
