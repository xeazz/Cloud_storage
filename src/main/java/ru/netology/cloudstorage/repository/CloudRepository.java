package ru.netology.cloudstorage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.netology.cloudstorage.entity.CloudFile;
import ru.netology.cloudstorage.model.Response.ResponseFile;

import java.util.List;
import java.util.Optional;

@Repository
public interface CloudRepository extends JpaRepository<CloudFile, String> {
    List<ResponseFile> findAllByUserId(Long userId);

    Optional<CloudFile> findByUserIdAndFilename(Long userId, String filename);
    void deleteByUserIdAndFilename(Long userId, String filename);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE CloudFile c SET c.filename = :filename  WHERE c.filename = :oldName AND c.userId = :userId")
    void updateFilenameByUser (@Param("userId") Long userId,
                               @Param("oldName") String filename,
                               @Param("filename") String newFilename);

}
