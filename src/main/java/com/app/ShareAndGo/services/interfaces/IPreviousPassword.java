package com.app.ShareAndGo.services.interfaces;

import com.app.ShareAndGo.entities.PreviousPassword;

import java.util.Set;

public interface IPreviousPassword {
    Set<PreviousPassword> getPreviousPasswords();
}
