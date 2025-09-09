package pro.Flick.advertisement.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import pro.Flick.advertisement.dto.AdInfoResponseDTO;
import pro.Flick.entity.QAdvertisement;
import pro.Flick.entity.QFile;

import static pro.Flick.entity.QAdvertisement.advertisement;
import static pro.Flick.entity.QFile.file;

@RequiredArgsConstructor
public class AdvertisementRepositoryImpl implements AdvertisementRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public AdInfoResponseDTO QfindAdInfo(Long videoId) {
        return queryFactory
                .select(Projections.constructor(AdInfoResponseDTO.class,
                        advertisement.id,
                        advertisement.title,
                        advertisement.uri,
                        file.storedFileName
                        ))
                .from(advertisement)
                .join(advertisement.file, file).on(advertisement.file.id.eq(file.id))
                .where(advertisement.video.id.eq(videoId))
                .fetchOne();
    }
}
