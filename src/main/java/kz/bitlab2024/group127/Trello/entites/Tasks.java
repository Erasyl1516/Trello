package kz.bitlab2024.group127.Trello.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_tasks")

public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="task_title")
    private String title;
    @Column(name="task_description")
    private String description;
    @Column(name="task_status")
    private int status;// 0 - todo, 1 - intest, 2 - done, 3 - failed
    @ManyToOne
    private Folders folder;// Many To One
}
