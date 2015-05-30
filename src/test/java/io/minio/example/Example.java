/*
 * Minimal Object Storage Library, (C) 2015 Minio, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.minio.example;

import com.google.api.client.util.IOUtils;
import io.minio.client.Client;
import io.minio.client.ExceptionIterator;
import io.minio.client.ObjectStat;
import io.minio.client.acl.Acl;
import io.minio.client.errors.ClientException;
import io.minio.client.messages.Item;
import io.minio.client.messages.ListAllMyBucketsResult;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Example {
    public static void main(String[] args) throws IOException, XmlPullParserException, ClientException {
        System.out.println("Example app");

        // play.minio.io - s3 compatible object storage
        Client client = Client.getClient("http://play.minio.io:9000");
        // Requires no credentials for play.minio.io

        // amazonaws.com - amazon s3 object storage
//         Client client = Client.getClient("https://s3.amazonaws.com");
//         client.setKeys("accessKey", "secretKey");

        // s3-us-west-2 - amazon s3 object storage in oregon
//        Client client = Client.getClient("https://s3-us-west-2.amazonaws.com");
//        client.setKeys("accessKey", "secretKey");

        // Set a user agent for your app
        client.addUserAgent("Example app", "0.1", "amd64");

        // create bucket
        client.makeBucket("mybucket", Acl.PUBLIC_READ_WRITE);

        // set bucket ACL
        client.setBucketACL("mybucket", Acl.PRIVATE);

        // list buckets
        ListAllMyBucketsResult allMyBucketsResult = client.listBuckets();
        System.out.println(allMyBucketsResult);

        // create object
        client.putObject("mybucket", "myobject", "application/octet-stream", 11, new ByteArrayInputStream("hello world".getBytes("UTF-8")));

        // list objects
        ExceptionIterator<Item> myObjects = client.listObjects("mybucket");
        System.out.println(myObjects);

        // get object metadata
        ObjectStat objectStat = client.statObject("mybucket", "myobject");
        System.out.println(objectStat);

        // get object
        InputStream object = client.getObject("mybucket", "myobject");
        try {
            System.out.println("Printing object: ");
            IOUtils.copy(object, System.out);
        } finally {
            object.close();
        }

	// remove object
        client.removeObject("mybucket", "myobject");

	// remove bucket
	client.removeBucket("mybucket");
    }
}
