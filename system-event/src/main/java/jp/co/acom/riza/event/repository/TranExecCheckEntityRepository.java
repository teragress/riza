package jp.co.acom.riza.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jp.co.acom.riza.event.entity.TranExecCheckEntity;

/**
 * 実行チェックテーブル用リポジトリー定義
 * 
 * @author teratani
 *
 */
public interface TranExecCheckEntityRepository extends JpaRepository<TranExecCheckEntity, String> {
}
