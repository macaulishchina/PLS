// **********************************************************************
//
// Copyright (c) 2003-2018 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.7.1
//
// <auto-generated>
//
// Generated from file `pls.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package top.macaulish.pls.service.ice.server;

import top.macaulish.pls.pojo.ice.ModelInfo;
import top.macaulish.pls.pojo.ice.ModelsInfoHelper;

public interface Model extends com.zeroc.Ice.Object {
    static final String[] _iceIds =
            {
                    "::Ice::Object",
                    "::PLS::Model"
            };
    final static String[] _iceOps =
            {
                    "consumeAbility",
                    "ice_id",
                    "ice_ids",
                    "ice_isA",
                    "ice_ping",
                    "queryAll",
                    "querySpecific"
            };

    static String ice_staticId() {
        return "::PLS::Model";
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_queryAll(Model obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current) {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        inS.readEmptyParams();
        ModelInfo[] ret = obj.queryAll(current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ModelsInfoHelper.write(ostr, ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_querySpecific(Model obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current) {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_modelGuid;
        iceP_modelGuid = istr.readString();
        inS.endReadParams();
        ModelInfo ret = obj.querySpecific(iceP_modelGuid, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ModelInfo.ice_write(ostr, ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_consumeAbility(Model obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current) {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_modelGuid;
        iceP_modelGuid = istr.readString();
        inS.endReadParams();
        int ret = obj.consumeAbility(iceP_modelGuid, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeInt(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    ModelInfo[] queryAll(com.zeroc.Ice.Current current);

    ModelInfo querySpecific(String modelGuid, com.zeroc.Ice.Current current);

    int consumeAbility(String modelGuid, com.zeroc.Ice.Current current);

    @Override
    default String[] ice_ids(com.zeroc.Ice.Current current) {
        return _iceIds;
    }

    @Override
    default String ice_id(com.zeroc.Ice.Current current) {
        return ice_staticId();
    }

    @Override
    default java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceDispatch(com.zeroc.IceInternal.Incoming in, com.zeroc.Ice.Current current)
            throws com.zeroc.Ice.UserException {
        int pos = java.util.Arrays.binarySearch(_iceOps, current.operation);
        if (pos < 0) {
            throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
        }

        switch (pos) {
            case 0: {
                return _iceD_consumeAbility(this, in, current);
            }
            case 1: {
                return com.zeroc.Ice.Object._iceD_ice_id(this, in, current);
            }
            case 2: {
                return com.zeroc.Ice.Object._iceD_ice_ids(this, in, current);
            }
            case 3: {
                return com.zeroc.Ice.Object._iceD_ice_isA(this, in, current);
            }
            case 4: {
                return com.zeroc.Ice.Object._iceD_ice_ping(this, in, current);
            }
            case 5: {
                return _iceD_queryAll(this, in, current);
            }
            case 6: {
                return _iceD_querySpecific(this, in, current);
            }
        }

        assert (false);
        throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
    }
}
