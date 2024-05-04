package com.payment.gateway.entity;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "block_transactions")
public class BlockChainTransactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use GenerationType.IDENTITY for auto-increment
    @Column(name = "id", updatable = false, nullable = false)
    private Long id; // Use Integer for numeric ID

    @Column(name = "network_name")
    private String networkName;

    @Column(name = "code")
    private String code;

    @Column(name = "sender")
    private String sender;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "amount")
    private String amount;

    @Column(name = "gas_fee")
    private String gasFee;

    @Column(name = "contract")
    private String contract;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "decimal")
    private Integer decimal;

    @Column(name = "tx_hash")
    private String txHash;

    @Column(name = "block")
    private String block;

    @Column(name = "status")
    private String status;

    @Column(name = "remark")
    private String remark;

    @Column(name = "transaction_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private LocalDateTime transactionTime;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date createdAt;

    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date updatedAt;
}
