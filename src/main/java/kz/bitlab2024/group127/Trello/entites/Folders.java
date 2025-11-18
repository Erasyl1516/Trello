package kz.bitlab2024.group127.Trello.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_folders")

public class Folders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
    @Column(name="folders_name")
   private String name;
    @ManyToMany
    @JoinTable(
            name = "t_folders_categories",
            joinColumns = @JoinColumn(name = "folders_id"),
            inverseJoinColumns = @JoinColumn(name = "categories_id")
    )
   private List<TaskCategories> categories = new ArrayList<>();
    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Tasks> tasks = new ArrayList<>();
}
