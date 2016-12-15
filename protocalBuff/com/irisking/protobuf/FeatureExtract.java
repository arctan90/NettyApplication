// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: iris.proto

package com.irisking.protobuf;

/**
 * Protobuf type {@code irisking.protobuf.FeatureExtract}
 *
 * <pre>
 *新定义接口的时候，先新建立一个message，然后在Protocol中添加这个message的实例
 *图像抽特征的请求
 * </pre>
 */
public  final class FeatureExtract extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:irisking.protobuf.FeatureExtract)
    FeatureExtractOrBuilder {
  // Use FeatureExtract.newBuilder() to construct.
  private FeatureExtract(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }
  private FeatureExtract() {
    appId_ = 0;
    orgId_ = 0;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private FeatureExtract(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry) {
    this();
    int mutable_bitField0_ = 0;
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!input.skipField(tag)) {
              done = true;
            }
            break;
          }
          case 8: {

            appId_ = input.readUInt32();
            break;
          }
          case 16: {

            orgId_ = input.readUInt32();
            break;
          }
          case 26: {
            com.irisking.protobuf.IrisData.Builder subBuilder = null;
            if (data_ != null) {
              subBuilder = data_.toBuilder();
            }
            data_ = input.readMessage(com.irisking.protobuf.IrisData.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(data_);
              data_ = subBuilder.buildPartial();
            }

            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw new RuntimeException(e.setUnfinishedMessage(this));
    } catch (java.io.IOException e) {
      throw new RuntimeException(
          new com.google.protobuf.InvalidProtocolBufferException(
              e.getMessage()).setUnfinishedMessage(this));
    } finally {
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.irisking.protobuf.ProtoMsg.internal_static_irisking_protobuf_FeatureExtract_descriptor;
  }

  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.irisking.protobuf.ProtoMsg.internal_static_irisking_protobuf_FeatureExtract_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.irisking.protobuf.FeatureExtract.class, com.irisking.protobuf.FeatureExtract.Builder.class);
  }

  public static final int APPID_FIELD_NUMBER = 1;
  private int appId_;
  /**
   * <code>optional uint32 appId = 1;</code>
   */
  public int getAppId() {
    return appId_;
  }

  public static final int ORGID_FIELD_NUMBER = 2;
  private int orgId_;
  /**
   * <code>optional uint32 orgId = 2;</code>
   */
  public int getOrgId() {
    return orgId_;
  }

  public static final int DATA_FIELD_NUMBER = 3;
  private com.irisking.protobuf.IrisData data_;
  /**
   * <code>optional .irisking.protobuf.IrisData data = 3;</code>
   */
  public boolean hasData() {
    return data_ != null;
  }
  /**
   * <code>optional .irisking.protobuf.IrisData data = 3;</code>
   */
  public com.irisking.protobuf.IrisData getData() {
    return data_ == null ? com.irisking.protobuf.IrisData.getDefaultInstance() : data_;
  }
  /**
   * <code>optional .irisking.protobuf.IrisData data = 3;</code>
   */
  public com.irisking.protobuf.IrisDataOrBuilder getDataOrBuilder() {
    return getData();
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (appId_ != 0) {
      output.writeUInt32(1, appId_);
    }
    if (orgId_ != 0) {
      output.writeUInt32(2, orgId_);
    }
    if (data_ != null) {
      output.writeMessage(3, getData());
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (appId_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt32Size(1, appId_);
    }
    if (orgId_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt32Size(2, orgId_);
    }
    if (data_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, getData());
    }
    memoizedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.irisking.protobuf.FeatureExtract)) {
      return super.equals(obj);
    }
    com.irisking.protobuf.FeatureExtract other = (com.irisking.protobuf.FeatureExtract) obj;

    boolean result = true;
    result = result && (getAppId()
        == other.getAppId());
    result = result && (getOrgId()
        == other.getOrgId());
    result = result && (hasData() == other.hasData());
    if (hasData()) {
      result = result && getData()
          .equals(other.getData());
    }
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptorForType().hashCode();
    hash = (37 * hash) + APPID_FIELD_NUMBER;
    hash = (53 * hash) + getAppId();
    hash = (37 * hash) + ORGID_FIELD_NUMBER;
    hash = (53 * hash) + getOrgId();
    if (hasData()) {
      hash = (37 * hash) + DATA_FIELD_NUMBER;
      hash = (53 * hash) + getData().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.irisking.protobuf.FeatureExtract parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.irisking.protobuf.FeatureExtract parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.irisking.protobuf.FeatureExtract parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.irisking.protobuf.FeatureExtract parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.irisking.protobuf.FeatureExtract parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static com.irisking.protobuf.FeatureExtract parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }
  public static com.irisking.protobuf.FeatureExtract parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input);
  }
  public static com.irisking.protobuf.FeatureExtract parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input, extensionRegistry);
  }
  public static com.irisking.protobuf.FeatureExtract parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static com.irisking.protobuf.FeatureExtract parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.irisking.protobuf.FeatureExtract prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessage.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code irisking.protobuf.FeatureExtract}
   *
   * <pre>
   *新定义接口的时候，先新建立一个message，然后在Protocol中添加这个message的实例
   *图像抽特征的请求
   * </pre>
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:irisking.protobuf.FeatureExtract)
      com.irisking.protobuf.FeatureExtractOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.irisking.protobuf.ProtoMsg.internal_static_irisking_protobuf_FeatureExtract_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.irisking.protobuf.ProtoMsg.internal_static_irisking_protobuf_FeatureExtract_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.irisking.protobuf.FeatureExtract.class, com.irisking.protobuf.FeatureExtract.Builder.class);
    }

    // Construct using com.irisking.protobuf.FeatureExtract.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      appId_ = 0;

      orgId_ = 0;

      if (dataBuilder_ == null) {
        data_ = null;
      } else {
        data_ = null;
        dataBuilder_ = null;
      }
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.irisking.protobuf.ProtoMsg.internal_static_irisking_protobuf_FeatureExtract_descriptor;
    }

    public com.irisking.protobuf.FeatureExtract getDefaultInstanceForType() {
      return com.irisking.protobuf.FeatureExtract.getDefaultInstance();
    }

    public com.irisking.protobuf.FeatureExtract build() {
      com.irisking.protobuf.FeatureExtract result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public com.irisking.protobuf.FeatureExtract buildPartial() {
      com.irisking.protobuf.FeatureExtract result = new com.irisking.protobuf.FeatureExtract(this);
      result.appId_ = appId_;
      result.orgId_ = orgId_;
      if (dataBuilder_ == null) {
        result.data_ = data_;
      } else {
        result.data_ = dataBuilder_.build();
      }
      onBuilt();
      return result;
    }

    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.irisking.protobuf.FeatureExtract) {
        return mergeFrom((com.irisking.protobuf.FeatureExtract)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.irisking.protobuf.FeatureExtract other) {
      if (other == com.irisking.protobuf.FeatureExtract.getDefaultInstance()) return this;
      if (other.getAppId() != 0) {
        setAppId(other.getAppId());
      }
      if (other.getOrgId() != 0) {
        setOrgId(other.getOrgId());
      }
      if (other.hasData()) {
        mergeData(other.getData());
      }
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.irisking.protobuf.FeatureExtract parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.irisking.protobuf.FeatureExtract) e.getUnfinishedMessage();
        throw e;
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private int appId_ ;
    /**
     * <code>optional uint32 appId = 1;</code>
     */
    public int getAppId() {
      return appId_;
    }
    /**
     * <code>optional uint32 appId = 1;</code>
     */
    public Builder setAppId(int value) {
      
      appId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional uint32 appId = 1;</code>
     */
    public Builder clearAppId() {
      
      appId_ = 0;
      onChanged();
      return this;
    }

    private int orgId_ ;
    /**
     * <code>optional uint32 orgId = 2;</code>
     */
    public int getOrgId() {
      return orgId_;
    }
    /**
     * <code>optional uint32 orgId = 2;</code>
     */
    public Builder setOrgId(int value) {
      
      orgId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional uint32 orgId = 2;</code>
     */
    public Builder clearOrgId() {
      
      orgId_ = 0;
      onChanged();
      return this;
    }

    private com.irisking.protobuf.IrisData data_ = null;
    private com.google.protobuf.SingleFieldBuilder<
        com.irisking.protobuf.IrisData, com.irisking.protobuf.IrisData.Builder, com.irisking.protobuf.IrisDataOrBuilder> dataBuilder_;
    /**
     * <code>optional .irisking.protobuf.IrisData data = 3;</code>
     */
    public boolean hasData() {
      return dataBuilder_ != null || data_ != null;
    }
    /**
     * <code>optional .irisking.protobuf.IrisData data = 3;</code>
     */
    public com.irisking.protobuf.IrisData getData() {
      if (dataBuilder_ == null) {
        return data_ == null ? com.irisking.protobuf.IrisData.getDefaultInstance() : data_;
      } else {
        return dataBuilder_.getMessage();
      }
    }
    /**
     * <code>optional .irisking.protobuf.IrisData data = 3;</code>
     */
    public Builder setData(com.irisking.protobuf.IrisData value) {
      if (dataBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        data_ = value;
        onChanged();
      } else {
        dataBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>optional .irisking.protobuf.IrisData data = 3;</code>
     */
    public Builder setData(
        com.irisking.protobuf.IrisData.Builder builderForValue) {
      if (dataBuilder_ == null) {
        data_ = builderForValue.build();
        onChanged();
      } else {
        dataBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>optional .irisking.protobuf.IrisData data = 3;</code>
     */
    public Builder mergeData(com.irisking.protobuf.IrisData value) {
      if (dataBuilder_ == null) {
        if (data_ != null) {
          data_ =
            com.irisking.protobuf.IrisData.newBuilder(data_).mergeFrom(value).buildPartial();
        } else {
          data_ = value;
        }
        onChanged();
      } else {
        dataBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>optional .irisking.protobuf.IrisData data = 3;</code>
     */
    public Builder clearData() {
      if (dataBuilder_ == null) {
        data_ = null;
        onChanged();
      } else {
        data_ = null;
        dataBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>optional .irisking.protobuf.IrisData data = 3;</code>
     */
    public com.irisking.protobuf.IrisData.Builder getDataBuilder() {
      
      onChanged();
      return getDataFieldBuilder().getBuilder();
    }
    /**
     * <code>optional .irisking.protobuf.IrisData data = 3;</code>
     */
    public com.irisking.protobuf.IrisDataOrBuilder getDataOrBuilder() {
      if (dataBuilder_ != null) {
        return dataBuilder_.getMessageOrBuilder();
      } else {
        return data_ == null ?
            com.irisking.protobuf.IrisData.getDefaultInstance() : data_;
      }
    }
    /**
     * <code>optional .irisking.protobuf.IrisData data = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilder<
        com.irisking.protobuf.IrisData, com.irisking.protobuf.IrisData.Builder, com.irisking.protobuf.IrisDataOrBuilder> 
        getDataFieldBuilder() {
      if (dataBuilder_ == null) {
        dataBuilder_ = new com.google.protobuf.SingleFieldBuilder<
            com.irisking.protobuf.IrisData, com.irisking.protobuf.IrisData.Builder, com.irisking.protobuf.IrisDataOrBuilder>(
                getData(),
                getParentForChildren(),
                isClean());
        data_ = null;
      }
      return dataBuilder_;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:irisking.protobuf.FeatureExtract)
  }

  // @@protoc_insertion_point(class_scope:irisking.protobuf.FeatureExtract)
  private static final com.irisking.protobuf.FeatureExtract DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.irisking.protobuf.FeatureExtract();
  }

  public static com.irisking.protobuf.FeatureExtract getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<FeatureExtract>
      PARSER = new com.google.protobuf.AbstractParser<FeatureExtract>() {
    public FeatureExtract parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      try {
        return new FeatureExtract(input, extensionRegistry);
      } catch (RuntimeException e) {
        if (e.getCause() instanceof
            com.google.protobuf.InvalidProtocolBufferException) {
          throw (com.google.protobuf.InvalidProtocolBufferException)
              e.getCause();
        }
        throw e;
      }
    }
  };

  public static com.google.protobuf.Parser<FeatureExtract> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<FeatureExtract> getParserForType() {
    return PARSER;
  }

  public com.irisking.protobuf.FeatureExtract getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

