package jp.co.acom.example.eventnotify.trade.entity;

import javax.persistence.Entity;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Entity(name = "TRADE_REVINFO")
@RevisionEntity
public class TradeRevinfo extends DefaultRevisionEntity {}
