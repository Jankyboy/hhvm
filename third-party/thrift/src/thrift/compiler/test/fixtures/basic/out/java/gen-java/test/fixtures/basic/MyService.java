/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

package test.fixtures.basic;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.service.*;
import com.facebook.thrift.client.*;
import com.facebook.thrift.client.ResponseWrapper;
import com.facebook.thrift.client.RpcOptions;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.*;
import java.util.*;
import reactor.core.publisher.Mono;

@SwiftGenerated
@com.facebook.swift.service.ThriftService("MyService")
public interface MyService extends java.io.Closeable, com.facebook.thrift.util.BlockingService {
    static com.facebook.thrift.server.RpcServerHandlerBuilder<MyService> serverHandlerBuilder(MyService _serverImpl) {
        return new com.facebook.thrift.server.RpcServerHandlerBuilder<MyService>(_serverImpl) {
                @java.lang.Override
                public com.facebook.thrift.server.RpcServerHandler build() {
                return new MyServiceRpcServerHandler(impl, eventHandlers);
            }
        };
    }

    static com.facebook.thrift.client.ClientBuilder<MyService> clientBuilder() {
        return new ClientBuilder<MyService>() {
            @java.lang.Override
            public MyService build(Mono<RpcClient> rpcClientMono) {
                MyService.Reactive _delegate =
                    new MyServiceReactiveClient(protocolId, rpcClientMono, headersMono, persistentHeadersMono);
                return new MyServiceReactiveBlockingWrapper(_delegate);
            }
        };
    }

    @com.facebook.swift.service.ThriftService("MyService")
    public interface Async extends java.io.Closeable, com.facebook.thrift.util.AsyncService {
        static com.facebook.thrift.server.RpcServerHandlerBuilder<MyService.Async> serverHandlerBuilder(MyService.Async _serverImpl) {
            return new com.facebook.thrift.server.RpcServerHandlerBuilder<MyService.Async>(_serverImpl) {
                @java.lang.Override
                public com.facebook.thrift.server.RpcServerHandler build() {
                    return new MyServiceRpcServerHandler(impl, eventHandlers);
                }
            };
        }

        static com.facebook.thrift.client.ClientBuilder<MyService.Async> clientBuilder() {
            return new ClientBuilder<MyService.Async>() {
                @java.lang.Override
                public MyService.Async build(Mono<RpcClient> rpcClientMono) {
                    MyService.Reactive _delegate =
                        new MyServiceReactiveClient(protocolId, rpcClientMono, headersMono, persistentHeadersMono);
                    return new MyServiceReactiveAsyncWrapper(_delegate);
                }
            };
        }

        @java.lang.Override void close();

        @ThriftMethod(value = "ping")
        ListenableFuture<Void> ping();

        default ListenableFuture<Void> ping(
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default ListenableFuture<ResponseWrapper<Void>> pingWrapper(
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "getRandomData")
        ListenableFuture<String> getRandomData();

        default ListenableFuture<String> getRandomData(
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default ListenableFuture<ResponseWrapper<String>> getRandomDataWrapper(
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "sink")
        ListenableFuture<Void> sink(
            @com.facebook.swift.codec.ThriftField(value=1, name="sink", requiredness=Requiredness.NONE) final long sink);

        default ListenableFuture<Void> sink(
            @com.facebook.swift.codec.ThriftField(value=1, name="sink", requiredness=Requiredness.NONE) final long sink,
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default ListenableFuture<ResponseWrapper<Void>> sinkWrapper(
            @com.facebook.swift.codec.ThriftField(value=1, name="sink", requiredness=Requiredness.NONE) final long sink,
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "putDataById")
        ListenableFuture<Void> putDataById(
            @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
            @com.facebook.swift.codec.ThriftField(value=2, name="data", requiredness=Requiredness.NONE) final String data);

        default ListenableFuture<Void> putDataById(
            @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
            @com.facebook.swift.codec.ThriftField(value=2, name="data", requiredness=Requiredness.NONE) final String data,
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default ListenableFuture<ResponseWrapper<Void>> putDataByIdWrapper(
            @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
            @com.facebook.swift.codec.ThriftField(value=2, name="data", requiredness=Requiredness.NONE) final String data,
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "hasDataById")
        ListenableFuture<Boolean> hasDataById(
            @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id);

        default ListenableFuture<Boolean> hasDataById(
            @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default ListenableFuture<ResponseWrapper<Boolean>> hasDataByIdWrapper(
            @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "getDataById")
        ListenableFuture<String> getDataById(
            @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id);

        default ListenableFuture<String> getDataById(
            @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default ListenableFuture<ResponseWrapper<String>> getDataByIdWrapper(
            @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "deleteDataById")
        ListenableFuture<Void> deleteDataById(
            @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id);

        default ListenableFuture<Void> deleteDataById(
            @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default ListenableFuture<ResponseWrapper<Void>> deleteDataByIdWrapper(
            @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "lobDataById",
                      oneway = true)
        ListenableFuture<Void> lobDataById(
            @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
            @com.facebook.swift.codec.ThriftField(value=2, name="data", requiredness=Requiredness.NONE) final String data);

        default ListenableFuture<Void> lobDataById(
            @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
            @com.facebook.swift.codec.ThriftField(value=2, name="data", requiredness=Requiredness.NONE) final String data,
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default ListenableFuture<ResponseWrapper<Void>> lobDataByIdWrapper(
            @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
            @com.facebook.swift.codec.ThriftField(value=2, name="data", requiredness=Requiredness.NONE) final String data,
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "invalid_return_for_hack")
        ListenableFuture<Set<Float>> invalidReturnForHack();

        default ListenableFuture<Set<Float>> invalidReturnForHack(
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default ListenableFuture<ResponseWrapper<Set<Float>>> invalidReturnForHackWrapper(
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "rpc_skipped_codegen")
        ListenableFuture<Void> rpcSkippedCodegen();

        default ListenableFuture<Void> rpcSkippedCodegen(
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default ListenableFuture<ResponseWrapper<Void>> rpcSkippedCodegenWrapper(
            RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }
    }
    @java.lang.Override void close();

    @ThriftMethod(value = "ping")
    void ping() throws org.apache.thrift.TException;

    default void ping(
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    default ResponseWrapper<Void> pingWrapper(
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    @ThriftMethod(value = "getRandomData")
    String getRandomData() throws org.apache.thrift.TException;

    default String getRandomData(
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    default ResponseWrapper<String> getRandomDataWrapper(
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    @ThriftMethod(value = "sink")
    void sink(
        @com.facebook.swift.codec.ThriftField(value=1, name="sink", requiredness=Requiredness.NONE) final long sink) throws org.apache.thrift.TException;

    default void sink(
        @com.facebook.swift.codec.ThriftField(value=1, name="sink", requiredness=Requiredness.NONE) final long sink,
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    default ResponseWrapper<Void> sinkWrapper(
        @com.facebook.swift.codec.ThriftField(value=1, name="sink", requiredness=Requiredness.NONE) final long sink,
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    @ThriftMethod(value = "putDataById")
    void putDataById(
        @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
        @com.facebook.swift.codec.ThriftField(value=2, name="data", requiredness=Requiredness.NONE) final String data) throws org.apache.thrift.TException;

    default void putDataById(
        @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
        @com.facebook.swift.codec.ThriftField(value=2, name="data", requiredness=Requiredness.NONE) final String data,
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    default ResponseWrapper<Void> putDataByIdWrapper(
        @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
        @com.facebook.swift.codec.ThriftField(value=2, name="data", requiredness=Requiredness.NONE) final String data,
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    @ThriftMethod(value = "hasDataById")
    boolean hasDataById(
        @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id) throws org.apache.thrift.TException;

    default boolean hasDataById(
        @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    default ResponseWrapper<Boolean> hasDataByIdWrapper(
        @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    @ThriftMethod(value = "getDataById")
    String getDataById(
        @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id) throws org.apache.thrift.TException;

    default String getDataById(
        @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    default ResponseWrapper<String> getDataByIdWrapper(
        @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    @ThriftMethod(value = "deleteDataById")
    void deleteDataById(
        @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id) throws org.apache.thrift.TException;

    default void deleteDataById(
        @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    default ResponseWrapper<Void> deleteDataByIdWrapper(
        @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    @ThriftMethod(value = "lobDataById",
                  oneway = true)
    void lobDataById(
        @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
        @com.facebook.swift.codec.ThriftField(value=2, name="data", requiredness=Requiredness.NONE) final String data) throws org.apache.thrift.TException;

    default void lobDataById(
        @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
        @com.facebook.swift.codec.ThriftField(value=2, name="data", requiredness=Requiredness.NONE) final String data,
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    default ResponseWrapper<Void> lobDataByIdWrapper(
        @com.facebook.swift.codec.ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final long id,
        @com.facebook.swift.codec.ThriftField(value=2, name="data", requiredness=Requiredness.NONE) final String data,
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    @ThriftMethod(value = "invalid_return_for_hack")
    Set<Float> invalidReturnForHack() throws org.apache.thrift.TException;

    default Set<Float> invalidReturnForHack(
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    default ResponseWrapper<Set<Float>> invalidReturnForHackWrapper(
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    @ThriftMethod(value = "rpc_skipped_codegen")
    void rpcSkippedCodegen() throws org.apache.thrift.TException;

    default void rpcSkippedCodegen(
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    default ResponseWrapper<Void> rpcSkippedCodegenWrapper(
        RpcOptions rpcOptions) throws org.apache.thrift.TException {
        throw new UnsupportedOperationException();
    }

    @com.facebook.swift.service.ThriftService("MyService")
    interface Reactive extends reactor.core.Disposable, com.facebook.thrift.util.ReactiveService {
        static com.facebook.thrift.server.RpcServerHandlerBuilder<MyService.Reactive> serverHandlerBuilder(MyService.Reactive _serverImpl) {
            return new com.facebook.thrift.server.RpcServerHandlerBuilder<MyService.Reactive>(_serverImpl) {
                @java.lang.Override
                public com.facebook.thrift.server.RpcServerHandler build() {
                    return new MyServiceRpcServerHandler(impl, eventHandlers);
                }
            };
        }

        static com.facebook.thrift.client.ClientBuilder<MyService.Reactive> clientBuilder() {
            return new ClientBuilder<MyService.Reactive>() {
                @java.lang.Override
                public MyService.Reactive build(Mono<RpcClient> rpcClientMono) {
                    return new MyServiceReactiveClient(protocolId, rpcClientMono, headersMono, persistentHeadersMono);
                }
            };
        }

        @ThriftMethod(value = "ping")
        reactor.core.publisher.Mono<Void> ping();

        default reactor.core.publisher.Mono<Void> ping(RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default reactor.core.publisher.Mono<ResponseWrapper<Void>> pingWrapper(RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "getRandomData")
        reactor.core.publisher.Mono<String> getRandomData();

        default reactor.core.publisher.Mono<String> getRandomData(RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default reactor.core.publisher.Mono<ResponseWrapper<String>> getRandomDataWrapper(RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "sink")
        reactor.core.publisher.Mono<Void> sink(final long sink);

        default reactor.core.publisher.Mono<Void> sink(final long sink, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default reactor.core.publisher.Mono<ResponseWrapper<Void>> sinkWrapper(final long sink, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "putDataById")
        reactor.core.publisher.Mono<Void> putDataById(final long id, final String data);

        default reactor.core.publisher.Mono<Void> putDataById(final long id, final String data, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default reactor.core.publisher.Mono<ResponseWrapper<Void>> putDataByIdWrapper(final long id, final String data, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "hasDataById")
        reactor.core.publisher.Mono<Boolean> hasDataById(final long id);

        default reactor.core.publisher.Mono<Boolean> hasDataById(final long id, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default reactor.core.publisher.Mono<ResponseWrapper<Boolean>> hasDataByIdWrapper(final long id, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "getDataById")
        reactor.core.publisher.Mono<String> getDataById(final long id);

        default reactor.core.publisher.Mono<String> getDataById(final long id, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default reactor.core.publisher.Mono<ResponseWrapper<String>> getDataByIdWrapper(final long id, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "deleteDataById")
        reactor.core.publisher.Mono<Void> deleteDataById(final long id);

        default reactor.core.publisher.Mono<Void> deleteDataById(final long id, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default reactor.core.publisher.Mono<ResponseWrapper<Void>> deleteDataByIdWrapper(final long id, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "lobDataById",
                  oneway = true)
        reactor.core.publisher.Mono<Void> lobDataById(final long id, final String data);

        default reactor.core.publisher.Mono<Void> lobDataById(final long id, final String data, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default reactor.core.publisher.Mono<ResponseWrapper<Void>> lobDataByIdWrapper(final long id, final String data, RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "invalid_return_for_hack")
        reactor.core.publisher.Mono<Set<Float>> invalidReturnForHack();

        default reactor.core.publisher.Mono<Set<Float>> invalidReturnForHack(RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default reactor.core.publisher.Mono<ResponseWrapper<Set<Float>>> invalidReturnForHackWrapper(RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        @ThriftMethod(value = "rpc_skipped_codegen")
        reactor.core.publisher.Mono<Void> rpcSkippedCodegen();

        default reactor.core.publisher.Mono<Void> rpcSkippedCodegen(RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

        default reactor.core.publisher.Mono<ResponseWrapper<Void>> rpcSkippedCodegenWrapper(RpcOptions rpcOptions) {
            throw new UnsupportedOperationException();
        }

    }
}
