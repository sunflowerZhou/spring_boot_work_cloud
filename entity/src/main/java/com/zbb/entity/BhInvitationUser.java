package com.zbb.entity;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author sunflower
 */
@Data
@Table(name = "bh_invitation_user_t")
public class BhInvitationUser implements Serializable {
    private Long id;

    private String extension;

    private String channel;

    private Long status;

    private Long joinAt;

    private String corpId;

    private String inviteUserid;

    private Long gmtModified;

    private String userId;

    private static final long serialVersionUID = 1L;
}